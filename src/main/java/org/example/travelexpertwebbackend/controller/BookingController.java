package org.example.travelexpertwebbackend.controller;

import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import org.example.travelexpertwebbackend.dto.ErrorInfo;
import org.example.travelexpertwebbackend.dto.GenericApiResponse;
import org.example.travelexpertwebbackend.dto.PaymentRequest;
import org.example.travelexpertwebbackend.dto.booking.BookingConfirmRequestDTO;
import org.example.travelexpertwebbackend.dto.booking.BookingCreateRequestDTO;
import org.example.travelexpertwebbackend.dto.booking.BookingCreateResponseDTO;
import org.example.travelexpertwebbackend.dto.booking.CostSummaryResponseDTO;
import org.example.travelexpertwebbackend.dto.wallet.CostSummaryRequestDTO;
import org.example.travelexpertwebbackend.entity.Customer;
import org.example.travelexpertwebbackend.service.BookingService;
import org.example.travelexpertwebbackend.service.StripeService;
import org.example.travelexpertwebbackend.service.auth.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tinylog.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;
    private final StripeService stripeService;

    public BookingController(BookingService bookingService, UserService userService, StripeService stripeService) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.stripeService = stripeService;
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


    @PostMapping("/create-payment-intent")
    public Map<String, Object> createPaymentIntent(@RequestBody PaymentRequest paymentRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            return stripeService.createStripePaymentIntent(paymentRequest);
        } catch (StripeException e) {
            Logger.error(e, "Failed to create payment intent");
            response.put("error", e.getMessage());
        }
        return response;
    }

    @PostMapping("/cost-summary")
    public ResponseEntity<GenericApiResponse<CostSummaryResponseDTO>>
    getPackageCostSummary(Authentication authentication, @Valid @RequestBody CostSummaryRequestDTO responseDTO) {
        String username = "";
        username = (String) authentication.getPrincipal();
        // find customer by username
        Customer customer = userService.getCustomerByUsername(username);
        CostSummaryResponseDTO bookingCreateResponseDTO = bookingService.getCostSummary(customer, responseDTO);
        return ResponseEntity.ok(new GenericApiResponse<>(bookingCreateResponseDTO));
    }

    @PostMapping("/confirm")
    public ResponseEntity<GenericApiResponse<BookingCreateResponseDTO>> confirmBooking(@Valid @RequestBody BookingConfirmRequestDTO responseDTO) {
        try {
            BookingCreateResponseDTO response = bookingService.confirmBooking(responseDTO.getBookingId(), responseDTO.getPaymentMethod(), responseDTO.getPaymentId());
            return ResponseEntity.ok(new GenericApiResponse<>(response));
        } catch (IllegalArgumentException | IllegalStateException e) {
            Logger.error(e, "Failed to confirm booking");
            return ResponseEntity.badRequest()
                    .body(new GenericApiResponse<>(List.of(new ErrorInfo(e.getMessage()))));
        } catch (Exception e) {
            Logger.error(e, "Failed to confirm booking");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericApiResponse<>(List.of(new ErrorInfo("An internal error occurred."))));
        }
    }

}
