package com.mna.springbootsecurity.payment.base.enums;

/**
 * Enumerates the supported payment gateway types.
 * This helps in type-safe selection of payment gateways.
 */
public enum PaymentGatewayType {
    RAZORPAY,
    STRIPE
    // Add more gateways here in the future, e.g., PAYPAL
}
