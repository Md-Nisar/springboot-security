package com.mna.springbootsecurity.payment.gateway;

import com.mna.springbootsecurity.payment.base.dto.PaymentRequestData;
import com.mna.springbootsecurity.payment.base.dto.PaymentResponseData;
import com.mna.springbootsecurity.payment.base.dto.WebhookPayload;
import com.mna.springbootsecurity.payment.base.enums.PaymentGatewayType;

/**
 * The core interface for the Strategy Pattern.
 * Defines the common operations that every payment gateway must support.
 */
public interface PaymentGateway {

    /**
     * Creates a payment order/intent with the payment provider.
     *
     * @param request The payment request containing amount, currency, etc.
     * @return A {@link PaymentResponseData} with details for the client (e.g., orderId, clientSecret).
     */
    PaymentResponseData createOrder(PaymentRequestData request);

    /**
     * Verifies the signature of an incoming webhook to ensure it's authentic.
     *
     * @param payload The webhook payload containing the raw data and signature.
     * @return True if the signature is valid, false otherwise.
     */
    boolean verifySignature(WebhookPayload payload);

    /**
     * Returns the specific type of the gateway.
     * This is used by the factory to identify the gateway.
     *
     * @return The {@link PaymentGatewayType} of the implementation.
     */
    PaymentGatewayType getType();
}