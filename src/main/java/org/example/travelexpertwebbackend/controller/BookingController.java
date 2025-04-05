package org.example.travelexpertwebbackend.controller;

import jakarta.validation.Valid;
import org.example.travelexpertwebbackend.dto.ErrorInfo;
import org.example.travelexpertwebbackend.dto.GenericApiResponse;
import org.example.travelexpertwebbackend.dto.booking.BookingCreateRequestDTO;
import org.example.travelexpertwebbackend.dto.booking.BookingCreateResponseDTO;
import org.example.travelexpertwebbackend.service.BookingService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping()
    public GenericApiResponse<BookingCreateResponseDTO> createBooking(Authentication authentication, @Valid @RequestBody BookingCreateRequestDTO responseDTO) {
        try {
            String username = (String) authentication.getPrincipal();
            BookingCreateResponseDTO bookingCreateResponseDTO = bookingService.createBooking(username, responseDTO);
            return new GenericApiResponse<>(bookingCreateResponseDTO);
        } catch (IllegalArgumentException e) {
            return new GenericApiResponse<>(List.of(new ErrorInfo(e.getMessage())));
        } catch (Exception e) {
            return new GenericApiResponse<>(List.of(new ErrorInfo("Failed to create booking.")));
        }
    }

}
