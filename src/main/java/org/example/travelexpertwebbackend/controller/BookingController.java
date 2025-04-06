package org.example.travelexpertwebbackend.controller;

import jakarta.validation.Valid;
import org.example.travelexpertwebbackend.dto.ErrorInfo;
import org.example.travelexpertwebbackend.dto.GenericApiResponse;
import org.example.travelexpertwebbackend.dto.booking.BookingCreateRequestDTO;
import org.example.travelexpertwebbackend.dto.booking.BookingCreateResponseDTO;
import org.example.travelexpertwebbackend.entity.Customer;
import org.example.travelexpertwebbackend.service.BookingService;
import org.example.travelexpertwebbackend.service.auth.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tinylog.Logger;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    public BookingController(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<GenericApiResponse<BookingCreateResponseDTO>> createBooking(Authentication authentication, @Valid @RequestBody BookingCreateRequestDTO responseDTO) {
        String username = "";
        try {
            username = (String) authentication.getPrincipal();
            // find customer by username
            Customer customer = userService.getCustomerByUsername(username);
            BookingCreateResponseDTO bookingCreateResponseDTO = bookingService.createBooking(customer, responseDTO);
            return ResponseEntity.ok(new GenericApiResponse<>(bookingCreateResponseDTO));
        } catch (IllegalArgumentException | IllegalStateException e) {
            Logger.error(e, "Failed to create booking with username: " + username);
            return ResponseEntity.badRequest().body(new GenericApiResponse<>(List.of(new ErrorInfo(e.getMessage()))));
        } catch (Exception e) {
            Logger.error(e, "Failed to create booking with username: " + username);
            return ResponseEntity.internalServerError().body(new GenericApiResponse<>(List.of(new ErrorInfo("Failed to create booking"))));
        }
    }

}
