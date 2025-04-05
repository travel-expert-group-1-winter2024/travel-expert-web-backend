package org.example.travelexpertwebbackend.dto.booking;

import java.math.BigDecimal;
import java.util.Optional;

public class BookingCreateResponseDTO {
    private int bookingId;
    private String bookingNo;
    private BigDecimal finalPrice;
    private int pointsEarned;
    private Optional<String> newCustomerTier;

    public BookingCreateResponseDTO() {
    }

    public BookingCreateResponseDTO(int bookingId, String bookingNo, BigDecimal finalPrice, int pointsEarned, Optional<String> newCustomerTier) {
        this.bookingId = bookingId;
        this.bookingNo = bookingNo;
        this.finalPrice = finalPrice;
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

    public Optional<String> getNewCustomerTier() {
        return newCustomerTier;
    }
}
