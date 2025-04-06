package org.example.travelexpertwebbackend.dto.booking;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

public class BookingCreateResponseDTO {
    private int bookingId;
    private String bookingNo;
    private BigDecimal discount;
    private BigDecimal finalPrice;
    private BigDecimal newBalance;
    private int pointsEarned;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String newCustomerTier;


    public BookingCreateResponseDTO() {
    }

    public BookingCreateResponseDTO(int bookingId, String bookingNo, BigDecimal discount, BigDecimal finalPrice, BigDecimal newBalance, int pointsEarned, String newCustomerTier) {
        this.bookingId = bookingId;
        this.bookingNo = bookingNo;
        this.discount = discount;
        this.finalPrice = finalPrice;
        this.newBalance = newBalance;
        this.pointsEarned = pointsEarned;
        this.newCustomerTier = newCustomerTier;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public int getPointsEarned() {
        return pointsEarned;
    }

    public String getNewCustomerTier() {
        return newCustomerTier;
    }

    public BigDecimal getNewBalance() {
        return newBalance;
    }

    public BigDecimal getDiscount() {
        return discount;
    }
}
