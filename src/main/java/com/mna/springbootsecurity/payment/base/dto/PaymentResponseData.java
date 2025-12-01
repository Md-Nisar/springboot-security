package com.mna.springbootsecurity.payment.base.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * Data Transfer Object for the response after initiating a payment.
 * Contains information needed by the client to proceed with the payment on the frontend.
 */
@Data
@Builder // Lombok: provides a builder pattern for object creation
public class PaymentResponseData {
    private String orderId; // The order ID from the payment gateway
    private String paymentId; // The payment ID (if available immediately, otherwise null)
    private String status;    // Status of the order creation (e.g., CREATED)
    private String clientSecret; // For gateways like Stripe (PaymentIntent client secret)
    private String apiKey;       // Public API key for frontend (e.g., Razorpay Key ID)
    private Map<String, Object> additionalDetails; // For any other gateway-specific details
}
