package org.example.travelexpertwebbackend.controller;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.validation.Valid;
import org.example.travelexpertwebbackend.dto.ErrorInfo;
import org.example.travelexpertwebbackend.dto.GenericApiResponse;
import org.example.travelexpertwebbackend.dto.PaymentRequest;
import org.example.travelexpertwebbackend.dto.booking.BookingConfirmRequestDTO;
import org.example.travelexpertwebbackend.dto.booking.BookingCreateRequestDTO;
import org.example.travelexpertwebbackend.dto.booking.BookingCreateResponseDTO;
import org.example.travelexpertwebbackend.dto.booking.CostSummaryResponseDTO;
import org.example.travelexpertwebbackend.entity.Customer;
import org.example.travelexpertwebbackend.service.BookingService;
import org.example.travelexpertwebbackend.service.auth.UserService;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

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


    @PostMapping("/create-payment-intent")
    public Map<String, Object> createPaymentIntent(@RequestBody PaymentRequest paymentRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            Stripe.apiKey = stripeSecretKey;
            // Create a payment intent with the amount and currency
            // You can modify the amount and currency based on your logic
            long amount = paymentRequest.getPackagePrice() * 100; // Amount in cents (1000 cents = $10.00)
            String currency = "CAD"; // Modify this if needed

            // Create payment intent
            PaymentIntent paymentIntent = PaymentIntent.create(
                    new HashMap<String, Object>() {{
                        put("amount", amount);
                        put("currency", currency);
                        // Additional params based on your requirements can go here
                    }}
            );

            // Return the clientSecret of the payment intent
            response.put("paymentIntentId", paymentIntent.getId());
            response.put("clientSecret", paymentIntent.getClientSecret());
        } catch (StripeException e) {
            // Handle error
            response.put("error", e.getMessage());
        }
        return response;
    }

    @PostMapping("/cost-summary")
    public ResponseEntity<GenericApiResponse<CostSummaryResponseDTO>>
    getPackageCostSummary(Authentication authentication, @Valid @RequestBody BookingCreateRequestDTO responseDTO) {
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
            BookingCreateResponseDTO response = bookingService.confirmBooking(responseDTO.getBookingId(), responseDTO.getPaymentMethod());
            return ResponseEntity.ok(new GenericApiResponse<>(response));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(new GenericApiResponse<>(List.of(new ErrorInfo(e.getMessage()))));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericApiResponse<>(List.of(new ErrorInfo("An internal error occurred."))));
        }
    }

}
