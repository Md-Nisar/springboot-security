package com.mna.springbootsecurity.payment.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for handling incoming webhook payloads.
 * This is a generic container; specific parsing might occur in gateway implementations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebhookPayload {
    private String rawPayload; // The raw JSON string or body of the webhook
    private String signature;  // Signature provided by the gateway for verification (e.g., X-Razorpay-Signature)
    // You might add a Map<String, String> headers if needed for verification
}
