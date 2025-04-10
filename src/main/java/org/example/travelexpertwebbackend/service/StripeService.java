package org.example.travelexpertwebbackend.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.example.travelexpertwebbackend.dto.PaymentRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    public PaymentIntent getPaymentIntent(String paymentIntentId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        return PaymentIntent.retrieve(paymentIntentId);
    }

    public Map<String, Object> createStripePaymentIntent(PaymentRequest paymentRequest) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        long amount = paymentRequest.getPackagePrice() * 100L; // convert dollars to cents
        String currency = "CAD";

        PaymentIntent paymentIntent = PaymentIntent.create(
                new HashMap<String, Object>() {{
                    put("amount", amount);
                    put("currency", currency);
                    // you can add metadata here if needed, e.g. put("metadata", ...)
                }}
        );

        Map<String, Object> response = new HashMap<>();
        response.put("paymentIntentId", paymentIntent.getId());
        response.put("clientSecret", paymentIntent.getClientSecret());

        return response;
    }
}
