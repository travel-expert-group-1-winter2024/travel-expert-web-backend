package org.example.travelexpertwebbackend.controller;

import org.example.travelexpertwebbackend.dto.booking.ReceiptEmailRequestDTO;
import org.example.travelexpertwebbackend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public Map<String, String> sendEmail(@RequestParam String to,
                                         @RequestParam String subject,
                                         @RequestParam String body) {
        Map<String, String> response = new HashMap<>();
        try {
            emailService.sendEmail(to, subject, body);
            response.put("message", "Email sent successfully!");
        } catch (MessagingException e) {
            response.put("error", "Failed to send email: " + e.getMessage());
        }
        return response;
    }

    @PostMapping("/send-booking-confirmation")
    public Map<String, String> sendBookingConfirmation(
            @RequestBody ReceiptEmailRequestDTO request) {

        Map<String, String> response = new HashMap<>();
        try {
            emailService.sendBookingConfirmation(
                    request.getTo(),
                    request.getSubject(),
                    request.getBody(),
                    request.getPdfBase64()
            );
            response.put("message", "Email sent successfully!");
        } catch (MessagingException e) {
            response.put("error", "Failed to send email: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            response.put("error", "Invalid PDF attachment: " + e.getMessage());
        }
        return response;
    }
}

