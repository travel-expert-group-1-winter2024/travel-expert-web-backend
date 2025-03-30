package org.example.travelexpertwebbackend.controller;

import org.example.travelexpertwebbackend.entity.BookingDetailsView;
import org.example.travelexpertwebbackend.service.BookingDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
