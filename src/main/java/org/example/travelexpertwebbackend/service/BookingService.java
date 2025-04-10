package org.example.travelexpertwebbackend.service;

import jakarta.transaction.Transactional;
import org.example.travelexpertwebbackend.dto.booking.BookingCreateRequestDTO;
import org.example.travelexpertwebbackend.dto.booking.BookingCreateResponseDTO;
import org.example.travelexpertwebbackend.entity.*;
import org.example.travelexpertwebbackend.entity.Package;
import org.example.travelexpertwebbackend.repository.*;
import org.example.travelexpertwebbackend.service.auth.UserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final PackageRepository packageRepository;
    private final TripTypesRepository tripTypesRepository;
    private final TransactionRepository transactionRepository;
    private final CustomerTierService customerTierService;
    private final UserService userService;
    private final BookingDetailRepository bookingDetailRepository;

    public BookingService(
            BookingRepository bookingRepository,
            PackageRepository packageRepository,
            TripTypesRepository tripTypesRepository,
            TransactionRepository transactionRepository,
            CustomerTierService customerTierService,
            UserService userService, BookingDetailRepository bookingDetailRepository) {
        this.bookingRepository = bookingRepository;
        this.packageRepository = packageRepository;
        this.tripTypesRepository = tripTypesRepository;
        this.customerTierService = customerTierService;
        this.userService = userService;
        this.transactionRepository = transactionRepository;
        this.bookingDetailRepository = bookingDetailRepository;
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
                } else { // less than 24 hours
                    throw new IllegalArgumentException("Package is already reserved");
                }
            }
        }

        // check if the tripTypeId is valid
        TripType tripType = tripTypesRepository.findById(requestDTO.getTripTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Trip Type not found with ID: " + requestDTO.getTripTypeId()));

        // create a new booking
        BigDecimal totalPrice = calculateTotalPrice(aPackage.getPkgbaseprice(), aPackage.getPkgagencycommission(), requestDTO.getTravelerCount());
        String bookingNo = generateBookingNumber();
        double travelerCount = requestDTO.getTravelerCount();
        BigDecimal discount = customerTierService.calculateDiscount(customer, customer.getCustomerTier(), totalPrice);
        int pointsEarned = calculatePointToEarn(totalPrice);
        BigDecimal finalPrice = calculateFinalPrice(totalPrice, discount);

        BigDecimal newBalance = null;
        boolean isReservation = requestDTO.getBookingMode() == BookingCreateRequestDTO.BookingMode.RESERVE;
        Instant reservedUntil = null;
        if (isReservation) {
            reservedUntil = Instant.now().plusSeconds(24 * 60 * 60); // 24 hours
        } else {
            newBalance = processPayment(requestDTO.getPaymentMethod(), customer, finalPrice, aPackage.getPkgname(), bookingNo);
        }

        Booking booking = new Booking();
        booking.setBookingDate(Instant.now());
        booking.setBookingNo(bookingNo);
        booking.setTravelerCount(travelerCount);
        booking.setTotalDiscount(discount);
        booking.setPointsEarned(pointsEarned);
        booking.setFinalPrice(finalPrice);
        booking.setBookingStatus(isReservation ? Booking.BookingStatus.RESERVED : Booking.BookingStatus.COMPLETED);
        booking.setReservedDatetime(reservedUntil);

        // set associated entities
        booking.setTripType(tripType);
        booking.setPackageid(aPackage);

        // create booking details
        String[] packageDestinations = aPackage.getDestination().replaceAll(" ", "").split(",");
        int destinationCount = packageDestinations.length;
        for (String destination : packageDestinations) {
            BookingDetail bookingDetail = new BookingDetail();
            bookingDetail.setItineraryno((double) (100 + (int) (Math.random() * 900))); // random itinerary number
            bookingDetail.setTripstart(aPackage.getPkgstartdate());
            bookingDetail.setTripend(aPackage.getPkgenddate());
            bookingDetail.setDescription(aPackage.getPkgdesc());
            bookingDetail.setDestination(destination);
            bookingDetail.setBaseprice(aPackage.getPkgbaseprice().divide(new BigDecimal(destinationCount), 2, RoundingMode.HALF_UP));
            bookingDetail.setAgencycommission(aPackage.getPkgagencycommission().divide(new BigDecimal(destinationCount), 2, RoundingMode.HALF_UP));
            // set booking detail to booking
            bookingDetail.setBooking(booking);
            booking.getBookingDetails().add(bookingDetail);
        }

        // update customer points
        customer.setPoints(customer.getPoints() + pointsEarned);

        // check customer tier
        CustomerTier customerTier = customerTierService.getTierByPoint(customer.getPoints());
        boolean isNewTier = false;
        if (!customerTier.getName().equals(customer.getCustomerTier().getName())) {
            customer.setCustomerTier(customerTier);
            isNewTier = true;
        }

        booking.setCustomer(customer);

        // save booking
        Booking savedBooking = bookingRepository.save(booking);
        return new BookingCreateResponseDTO(
                savedBooking.getId(),
                savedBooking.getBookingNo(),
                discount,
                savedBooking.getFinalPrice(),
                newBalance, // optional
                pointsEarned,
                isNewTier ? customerTier.getName() : null, // optional
                savedBooking.getBookingStatus().name(),
                savedBooking.getReservedDatetime(), // optional
                isReservation
        );
    }

    private BigDecimal processPayment(PaymentMethod paymentMethod, Customer customer, BigDecimal finalPrice, String pkgName, String bookingNo) {
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

    public enum PaymentMethod {
        WALLET,
        STRIPE,
    }

}
