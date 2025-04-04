package org.example.travelexpertwebbackend.controller;

import org.example.travelexpertwebbackend.dto.ErrorInfo;
import org.example.travelexpertwebbackend.dto.GenericApiResponse;
import org.example.travelexpertwebbackend.entity.BookingDetailsView;
import org.example.travelexpertwebbackend.service.BookingDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tinylog.Logger;

import java.util.List;

@RestController
@RequestMapping("/api/bookingdetails")
public class BookingDetailController {

    @Autowired
    private BookingDetailService bookingDetailService;

    @GetMapping
    public ResponseEntity<List<BookingDetailsView>> getAllBookings() {
        List<BookingDetailsView> bookingDetails = bookingDetailService.getAllBookingDetails();
        return ResponseEntity.ok(bookingDetails);
    }

    @GetMapping("/{customerid}")
    public ResponseEntity<GenericApiResponse<List<BookingDetailsView>>> getAllBookingsforCustomer(@PathVariable Integer customerid) {
        try {
            Logger.debug("Getting Booking Details for " + customerid);
            List<BookingDetailsView> bookingDetails = bookingDetailService.getAllBookingDetailsforCustomer(customerid);

            if (bookingDetails == null || bookingDetails.isEmpty()) {
                Logger.warn("No bookings found for customer ID " + customerid);
                return ResponseEntity.ok(new GenericApiResponse<>(bookingDetails));
            }

            Logger.info("Successfully Got Booking Details " + customerid);
            return ResponseEntity.ok(new GenericApiResponse<>(bookingDetails));
        } catch (Exception e) {
            Logger.error("Error fetching bookings for customer ID " + customerid + ": " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericApiResponse<>(List.of(new ErrorInfo("An internal error occurred."))));
        }
    }
}
