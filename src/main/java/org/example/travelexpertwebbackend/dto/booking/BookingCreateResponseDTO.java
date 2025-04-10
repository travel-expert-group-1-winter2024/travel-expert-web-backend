package org.example.travelexpertwebbackend.dto.booking;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.example.travelexpertwebbackend.service.BookingService;

import java.math.BigDecimal;
import java.time.Instant;

public class BookingCreateResponseDTO {
    private int bookingId;
    private String bookingNo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal discount;
    private BigDecimal finalPrice;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal newBalance; // optional
    private int pointsEarned;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String newCustomerTier; // optional
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Instant reservedUntil; // optional
    private Boolean paymentDue;
    private BookingService.PaymentMethod paymentMethod;
    private Integer packageId;

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

    public BookingCreateResponseDTO(int bookingId, String bookingNo, BigDecimal discount, BigDecimal finalPrice, BigDecimal newBalance, int pointsEarned, String newCustomerTier, String status, Instant reservedUntil, Boolean paymentDue, BookingService.PaymentMethod paymentMethod, Integer packageId) {
        this.bookingId = bookingId;
        this.bookingNo = bookingNo;
        this.discount = discount;
        this.finalPrice = finalPrice;
        this.newBalance = newBalance;
        this.pointsEarned = pointsEarned;
        this.newCustomerTier = newCustomerTier;
        this.status = status;
        this.reservedUntil = reservedUntil;
        this.paymentDue = paymentDue;
        this.paymentMethod = paymentMethod;
        this.packageId = packageId;
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

    public String getStatus() {
        return status;
    }

    public Instant getReservedUntil() {
        return reservedUntil;
    }

    public Boolean getPaymentDue() {
        return paymentDue;
    }

    public BookingService.PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public Integer getPackageId() {
        return packageId;
    }
}
