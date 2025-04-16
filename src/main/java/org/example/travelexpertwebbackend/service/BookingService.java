package org.example.travelexpertwebbackend.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
import org.example.travelexpertwebbackend.dto.booking.BookingCreateRequestDTO;
import org.example.travelexpertwebbackend.dto.booking.BookingCreateResponseDTO;
import org.example.travelexpertwebbackend.dto.booking.CostSummaryResponseDTO;
import org.example.travelexpertwebbackend.dto.wallet.CostSummaryRequestDTO;
import org.example.travelexpertwebbackend.entity.*;
import org.example.travelexpertwebbackend.entity.Package;
import org.example.travelexpertwebbackend.repository.*;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final PackageRepository packageRepository;
    private final TripTypesRepository tripTypesRepository;
    private final TransactionRepository transactionRepository;
    private final CustomerTierService customerTierService;
    private final StripeService stripeService;
    private final RegionRepository regionRepository;
    private final ClassRepository classRepository;
    private final FeeRepository feeRepository;
    private final PackagesProductsSupplierRepository packagesProductsSupplierRepository;

    public BookingService(
            BookingRepository bookingRepository,
            PackageRepository packageRepository,
            TripTypesRepository tripTypesRepository,
            TransactionRepository transactionRepository,
            CustomerTierService customerTierService, StripeService stripeService, RegionRepository regionRepository, ClassRepository classRepository, FeeRepository feeRepository, PackagesProductsSupplierRepository packagesProductsSupplierRepository) {
        this.bookingRepository = bookingRepository;
        this.packageRepository = packageRepository;
        this.tripTypesRepository = tripTypesRepository;
        this.transactionRepository = transactionRepository;
        this.customerTierService = customerTierService;
        this.stripeService = stripeService;
        this.regionRepository = regionRepository;
        this.classRepository = classRepository;
        this.feeRepository = feeRepository;
        this.packagesProductsSupplierRepository = packagesProductsSupplierRepository;
    }


    // Create a new booking
    @Transactional
    public BookingCreateResponseDTO createBooking(Customer customer, BookingCreateRequestDTO requestDTO) {
        // check if the packageId is valid
        Package aPackage = packageRepository.findById(requestDTO.getPackageId())
                .orElseThrow(() -> new IllegalArgumentException("Package not found with ID: " + requestDTO.getPackageId())
                );

        // check if package is available
        for (Booking booking : aPackage.getBookings()) {
            if (booking.getBookingStatus() == Booking.BookingStatus.RESERVED) {
                if (booking.getReservedDatetime().isBefore(Instant.now())) { // more than 24 hours
                    booking.setBookingStatus(Booking.BookingStatus.EXPIRED);
                    bookingRepository.save(booking);
                }
//                else { // less than 24 hours
//                    throw new IllegalArgumentException("Package is already reserved");
//                }
            }
        }

        // check if the tripTypeId is valid
        TripType tripType = tripTypesRepository.findById(requestDTO.getTripTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Trip Type not found with ID: " + requestDTO.getTripTypeId()));

        boolean isReservation = isReservationMode(requestDTO.getBookingMode());

        // check Stripe payment method's requirement
        if (!isReservation) checkStripePaymentRequirement(requestDTO.getPaymentMethod(), requestDTO.getPaymentId());

        // create a new booking
        BigDecimal totalPrice = calculateTotalPrice(aPackage.getPkgbaseprice(), aPackage.getPkgagencycommission(), requestDTO.getTravelerCount());
        String bookingNo = generateBookingNumber();
        double travelerCount = requestDTO.getTravelerCount();
        BigDecimal discount = customerTierService.calculateDiscount(customer, customer.getCustomerTier(), totalPrice);
        Integer pointsEarned = calculatePointToEarn(totalPrice);
        BigDecimal finalPrice = calculateFinalPrice(totalPrice, discount);


        Instant reservedUntil = isReservation ? getReservationExpiry() : null;
        BigDecimal newBalance = null;
        try {
            newBalance = isReservation ? null : processPayment(requestDTO.getPaymentMethod(), customer, finalPrice, aPackage.getPkgname(), bookingNo, requestDTO.getPaymentId());
        } catch (StripeException e) {
            Logger.error(e, "Error processing payment");
            throw new RuntimeException(e);
        }

        Booking booking = new Booking();
        booking.setBookingDate(Instant.now());
        booking.setBookingNo(bookingNo);
        booking.setTravelerCount(travelerCount);
        booking.setTotalDiscount(discount);
        booking.setPointsEarned(isReservation ? 0 : pointsEarned);
        booking.setFinalPrice(finalPrice);
        booking.setBookingStatus(isReservation ? Booking.BookingStatus.RESERVED : Booking.BookingStatus.COMPLETED);
        booking.setReservedDatetime(reservedUntil);

        // set associated entities
        booking.setTripType(tripType);
        booking.setPackageid(aPackage);

        // create booking details
        Region region = regionRepository.findById("OTHR").orElseThrow(() -> new IllegalArgumentException("Region not found with ID: OTHR"));
        Classes classes = classRepository.findById("ECN").orElseThrow(() -> new IllegalArgumentException("Class not found with ID: ECN"));
        Fee fee = feeRepository.findById("BK").orElseThrow(() -> new IllegalArgumentException("Fee not found with ID: BK"));
        List<PackagesProductsSupplier> ppsList = packagesProductsSupplierRepository.findByPackageid_Id(aPackage.getId());
        if (ppsList.isEmpty()) {
            throw new IllegalArgumentException("No products found for the package");
        }
        PackagesProductsSupplier selectedProduct = ppsList.get((int) (Math.random() * ppsList.size()));

        BookingDetail bookingDetail = new BookingDetail();
        bookingDetail.setItineraryno((double) (100 + (int) (Math.random() * 900))); // random itinerary number
        bookingDetail.setTripstart(aPackage.getPkgstartdate());
        bookingDetail.setTripend(aPackage.getPkgenddate());
        bookingDetail.setDescription(aPackage.getPkgdesc());
        bookingDetail.setDestination(aPackage.getDestination());
        bookingDetail.setBaseprice(aPackage.getPkgbaseprice());
        bookingDetail.setAgencycommission(aPackage.getPkgagencycommission());
        bookingDetail.setRegionid(region);
        bookingDetail.setClassid(classes);
        bookingDetail.setFeeid(fee);
        bookingDetail.setProductsupplierid(selectedProduct.getProductsupplierid().getId());
        
        // set booking detail to booking
        bookingDetail.setBooking(booking);
        booking.getBookingDetails().add(bookingDetail);

        CustomerTier customerTier = null;
        boolean isNewTier = false;

        if (!isReservation) {
            customer.setPoints(customer.getPoints() + pointsEarned);
            customerTier = updateCustomerTierIfEligible(customer);
            isNewTier = customerTier != null;
        }

        booking.setCustomer(customer);
        List<String> travellerNamesList = requestDTO.getTravellerNames();
        if (travellerNamesList == null || travellerNamesList.isEmpty()) {
            throw new IllegalArgumentException("Traveller names cannot be empty");
        }

        String travellerNameCsv = String.join(",", travellerNamesList);
        booking.setTravellerName(travellerNameCsv);
        // save booking
        Booking savedBooking = bookingRepository.save(booking);
        return new BookingCreateResponseDTO(
                savedBooking.getId(),
                savedBooking.getBookingNo(),
                savedBooking.getTotalDiscount(),
                savedBooking.getFinalPrice(),
                newBalance, // optional
                savedBooking.getPointsEarned(),
                isNewTier ? customerTier.getName() : null, // optional
                savedBooking.getBookingStatus().name(),
                savedBooking.getReservedDatetime(), // optional
                isReservation,
                requestDTO.getPaymentMethod(),
                savedBooking.getPackageid().getId()
        );
    }

    @Transactional
    public BookingCreateResponseDTO confirmBooking(Integer bookingId, PaymentMethod paymentMethod, String paymentId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));

        if (booking.getBookingStatus() != Booking.BookingStatus.RESERVED) {
            throw new IllegalStateException("Only reserved bookings can be confirmed.");
        }

        if (booking.getReservedDatetime() == null || booking.getReservedDatetime().isBefore(Instant.now())) {
            booking.setBookingStatus(Booking.BookingStatus.EXPIRED);
            bookingRepository.save(booking);
            throw new IllegalStateException("Reservation has expired and cannot be confirmed.");
        }

        checkStripePaymentRequirement(paymentMethod, paymentId);

        Customer customer = booking.getCustomer();
        BigDecimal finalPrice = booking.getFinalPrice();
        BigDecimal discount = booking.getTotalDiscount();
        Package aPackage = booking.getPackageid();

        // Perform payment
        BigDecimal newBalance = null;
        try {
            newBalance = processPayment(paymentMethod, customer, finalPrice, aPackage.getPkgname(), booking.getBookingNo(), paymentId);
        } catch (StripeException e) {
            Logger.error(e, "Error processing payment");
            throw new RuntimeException(e);
        }

        // checking and upgrading customer tier
        BigDecimal priceBeforeDiscount = finalPrice.subtract(discount);
        int pointsEarned = calculatePointToEarn(priceBeforeDiscount);

        customer.setPoints(customer.getPoints() + pointsEarned);
        CustomerTier customerTier = updateCustomerTierIfEligible(customer);
        boolean isNewTier = customerTier != null;

        // Update booking status
        booking.setCustomer(customer);
        booking.setPointsEarned(pointsEarned);
        booking.setBookingStatus(Booking.BookingStatus.COMPLETED);
        booking.setReservedDatetime(null);
        bookingRepository.save(booking);

        return new BookingCreateResponseDTO(
                booking.getId(),
                booking.getBookingNo(),
                null, // discount already applied earlier
                booking.getFinalPrice(),
                newBalance,
                booking.getPointsEarned(),
                isNewTier ? customerTier.getName() : null,
                booking.getBookingStatus().name(),
                null,
                false,
                paymentMethod,
                booking.getPackageid().getId()
        );
    }

    private BigDecimal processPayment(PaymentMethod paymentMethod, Customer customer, BigDecimal finalPrice, String pkgName, String bookingNo, String paymentId) throws StripeException {
        switch (paymentMethod) {
            case WALLET -> {
                Wallet wallet = customer.getWallet();
                if (wallet.getBalance().compareTo(finalPrice) < 0) {
                    throw new IllegalArgumentException("Insufficient balance in wallet");
                }
                BigDecimal newBalance = wallet.getBalance().subtract(finalPrice);
                wallet.setBalance(newBalance);

                Transaction transaction = new Transaction();
                transaction.setTransactionDate(Instant.now());
                transaction.setAmount(finalPrice);
                transaction.setTransactionType(Transaction.TransactionType.DEBIT);
                transaction.setDescription("Booking for " + pkgName + " with booking number " + bookingNo);
                transaction.setWallet(wallet);

                Transaction savedTransaction = transactionRepository.save(transaction);
                wallet.getTransactions().add(savedTransaction);
                customer.setWallet(wallet);
                return newBalance;
            }
            case STRIPE -> {

                PaymentIntent paymentIntent = stripeService.getPaymentIntent(paymentId);
                String status = paymentIntent.getStatus();

                if (!"succeeded".equals(status)) {
                    throw new IllegalArgumentException("Payment failed with status: " + status);
                }
                Transaction transaction = new Transaction();
                transaction.setTransactionDate(Instant.now());
                transaction.setAmount(finalPrice);
                transaction.setTransactionType(Transaction.TransactionType.DEBIT);
                transaction.setDescription("Booking for " + pkgName + " with booking number " + bookingNo);
                transaction.setStripeReference(paymentId);
                transactionRepository.save(transaction);

                return null;
            }
            default -> throw new UnsupportedOperationException("Unsupported payment method: " + paymentMethod);
        }
    }

    private String generateBookingNumber() {
        StringBuilder letters = new StringBuilder();
        StringBuilder digits = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            letters.append((char) ('A' + (int) (Math.random() * 26)));
            digits.append((int) (Math.random() * 10));
        }
        return letters + digits.toString();
    }

    private BigDecimal calculateTotalPrice(BigDecimal basePrice, BigDecimal agencyCommission, int travelerCount) {
        return basePrice.add(agencyCommission).multiply(new BigDecimal(travelerCount));
    }

    private int calculatePointToEarn(BigDecimal totalPrice) {
        return totalPrice.intValue(); // 1 dollar equals to 1 point
    }

    private BigDecimal calculateFinalPrice(BigDecimal totalPrice, BigDecimal discount) {
        return totalPrice.subtract(discount);
    }

    private boolean isReservationMode(BookingMode mode) {
        return BookingMode.RESERVE.equals(mode);
    }

    private Instant getReservationExpiry() {
        return Instant.now().plusSeconds(24 * 60 * 60); // 24 hours
    }

    private void checkStripePaymentRequirement(PaymentMethod paymentMethod, String paymentId) {
        if (paymentMethod.equals(PaymentMethod.STRIPE) && paymentId.isEmpty()) {
            throw new IllegalArgumentException("Payment ID is required for Stripe payment method");
        }
    }

    private CustomerTier updateCustomerTierIfEligible(Customer customer) {
        CustomerTier currentTier = customer.getCustomerTier();
        CustomerTier newTier = customerTierService.getTierByPoint(customer.getPoints());

        if (!newTier.getName().equals(currentTier.getName())) {
            customer.setCustomerTier(newTier);
            return newTier;
        }
        return null;
    }

    @Transactional
    public CostSummaryResponseDTO getCostSummary(Customer customer, CostSummaryRequestDTO requestDTO) {
        Package aPackage = packageRepository.findById(requestDTO.getPackageId())
                .orElseThrow(() -> new IllegalArgumentException("Package not found with ID: " + requestDTO.getPackageId()));

        TripType tripType = tripTypesRepository.findById(requestDTO.getTripTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Trip Type not found with ID: " + requestDTO.getTripTypeId()));

        BigDecimal totalPackagePrice = calculateTotalPrice(
                aPackage.getPkgbaseprice(),
                aPackage.getPkgagencycommission(),
                requestDTO.getTravelerCount());

        BigDecimal discount = customerTierService.calculateDiscount(customer, customer.getCustomerTier(), totalPackagePrice);
        BigDecimal packageSubtotal = calculateFinalPrice(totalPackagePrice, discount);

        BigDecimal charges;
        switch (tripType.getId()) {
            case "B":
            case "L":
                charges = BigDecimal.valueOf(25);
                break;
            case "G":
                charges = BigDecimal.valueOf(100);
                break;
            default:
                throw new IllegalArgumentException("Unknown trip type ID: " + tripType.getId());
        }

        BigDecimal agencyFees = aPackage.getPkgagencycommission();
        BigDecimal finalPriceWithoutTax = getfinalPriceWithoutTax(packageSubtotal, charges, agencyFees);
        BigDecimal tax = getTax(finalPriceWithoutTax);
        BigDecimal totalPrice = finalPriceWithoutTax.add(tax);

        CostSummaryResponseDTO response = new CostSummaryResponseDTO();
        response.setPackagePrice(totalPackagePrice);
        response.setDiscount(discount);
        response.setCharges(charges);
        response.setAgencyFees(agencyFees);
        response.setTax(tax);
        response.setTotal(totalPrice);

        return response;
    }

    public BigDecimal getfinalPriceWithoutTax(BigDecimal packageSubtotal, BigDecimal charges, BigDecimal agencyFees) {
        return packageSubtotal.add(charges).add(agencyFees);
    }

    public BigDecimal getTax(BigDecimal withoutTaxPrice) {
        return withoutTaxPrice.multiply(BigDecimal.valueOf(5)).divide(BigDecimal.valueOf(100));
    }


    public enum PaymentMethod {
        WALLET,
        STRIPE,
    }

    public enum BookingMode {
        NORMAL,
        RESERVE
    }

}
