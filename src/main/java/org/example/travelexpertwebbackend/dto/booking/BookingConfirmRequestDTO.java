package org.example.travelexpertwebbackend.dto.booking;

import jakarta.validation.constraints.NotNull;
import org.example.travelexpertwebbackend.service.BookingService;

public class BookingConfirmRequestDTO {
    @NotNull(message = "Booking ID cannot be null")
    private Integer bookingId;
    @NotNull(message = "Payment method cannot be null")
    private BookingService.PaymentMethod paymentMethod;

    public BookingConfirmRequestDTO() {
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public BookingService.PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(BookingService.PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
