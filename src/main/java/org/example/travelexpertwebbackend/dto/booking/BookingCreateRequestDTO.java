package org.example.travelexpertwebbackend.dto.booking;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.example.travelexpertwebbackend.service.BookingService;

public class BookingCreateRequestDTO {
    @Min(0)
    @NotNull(message = "Package ID cannot be null")
    private int packageId;
    @NotNull(message = "Trip Type ID cannot be null")
    private String tripTypeId;
    @NotNull(message = "Traveler count cannot be null")
    @Min(1)
    @Max(8)
    private int travelerCount;
    @NotNull(message = "Booking mode cannot be null")
    private BookingMode bookingMode = BookingMode.NORMAL;
    @NotNull(message = "Payment method cannot be null")
    private BookingService.PaymentMethod paymentMethod;

    public BookingCreateRequestDTO() {
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getTripTypeId() {
        return tripTypeId;
    }

    public void setTripTypeId(String tripTypeId) {
        this.tripTypeId = tripTypeId;
    }

    public int getTravelerCount() {
        return travelerCount;
    }

    public void setTravelerCount(int travelerCount) {
        this.travelerCount = travelerCount;
    }

    public BookingMode getBookingMode() {
        return bookingMode;
    }

    public void setBookingMode(BookingMode bookingMode) {
        this.bookingMode = bookingMode;
    }

    public BookingService.PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(BookingService.PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public enum BookingMode {
        NORMAL,
        RESERVE
    }

}
