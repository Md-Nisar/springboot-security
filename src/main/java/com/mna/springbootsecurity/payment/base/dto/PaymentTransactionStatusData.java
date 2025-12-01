package com.mna.springbootsecurity.payment.base.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for conveying the status of a payment transaction.
 * This DTO is used by the controller to send a simplified view of the transaction
 * status back to the client.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentTransactionStatusData {
    private Long id;
    private Long transactionId;
    private String status;
    private String orderId; // The order ID from the payment gateway
    private String gateway; // The payment gateway used (e.g., "RAZORPAY")
    private BigDecimal amount;
    private String currency;
    private String details; // Any additional details about the status
}
