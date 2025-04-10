package org.example.travelexpertwebbackend.service;

import jakarta.transaction.Transactional;
import org.example.travelexpertwebbackend.dto.booking.BookingCreateRequestDTO;
import org.example.travelexpertwebbackend.dto.booking.BookingCreateResponseDTO;
import org.example.travelexpertwebbackend.dto.booking.CostSummaryResponseDTO;
import org.example.travelexpertwebbackend.entity.*;
import org.example.travelexpertwebbackend.entity.Package;
import org.example.travelexpertwebbackend.repository.BookingRepository;
import org.example.travelexpertwebbackend.repository.PackageRepository;
import org.example.travelexpertwebbackend.repository.TransactionRepository;
import org.example.travelexpertwebbackend.repository.TripTypesRepository;
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

    public BookingService(
            BookingRepository bookingRepository,
            PackageRepository packageRepository,
            TripTypesRepository tripTypesRepository,
            TransactionRepository transactionRepository,
            CustomerTierService customerTierService,
            UserService userService) {
        this.bookingRepository = bookingRepository;
        this.packageRepository = packageRepository;
        this.tripTypesRepository = tripTypesRepository;
        this.customerTierService = customerTierService;
        this.userService = userService;
        this.transactionRepository = transactionRepository;
    }

    // Create a new booking
    @Transactional
    public BookingCreateResponseDTO createBooking(Customer customer, BookingCreateRequestDTO requestDTO) {
        // check if the packageId is valid
        Package aPackage = packageRepository.findById(requestDTO.getPackageId())
                .orElseThrow(() -> new IllegalArgumentException("Package not found with ID: " + requestDTO.getPackageId())
                );

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

        // check money in wallet
        Wallet wallet = customer.getWallet();
//        if (wallet.getBalance().compareTo(finalPrice) < 0) {
//            throw new IllegalArgumentException("Insufficient balance in wallet");
//        }
        // deduct money from wallet
        BigDecimal newBalance = wallet.getBalance().subtract(finalPrice);
        wallet.setBalance(newBalance);

        // add record in transaction
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(Instant.now());
        transaction.setAmount(finalPrice);
        transaction.setTransactionType(Transaction.TransactionType.DEBIT);
        transaction.setDescription("Booking for " + aPackage.getPkgname() + " with booking number " + bookingNo);
        transaction.setWallet(wallet);
        Transaction savedTransaction = transactionRepository.save(transaction);

        // add transaction to wallet
        wallet.getTransactions().add(savedTransaction);
        customer.setWallet(wallet);

        Booking booking = new Booking();
        booking.setBookingDate(Instant.now());
        booking.setBookingNo(bookingNo);
        booking.setTravelerCount(travelerCount);
        booking.setTotalDiscount(discount);
        booking.setPointsEarned(pointsEarned);
        booking.setFinalPrice(finalPrice);

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
                newBalance,
                pointsEarned,
                isNewTier ? customerTier.getName() : null
        );
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

    @Transactional
    public CostSummaryResponseDTO getCostSummary(Customer customer, BookingCreateRequestDTO requestDTO) {
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


    public BigDecimal getfinalPriceWithoutTax(BigDecimal packageSubtotal, BigDecimal charges, BigDecimal agencyFees){
        return packageSubtotal.add(charges).add(agencyFees);
    }

    public BigDecimal getTax(BigDecimal withoutTaxPrice){
        return withoutTaxPrice.multiply(BigDecimal.valueOf(5)).divide(BigDecimal.valueOf(100));
    }

}
