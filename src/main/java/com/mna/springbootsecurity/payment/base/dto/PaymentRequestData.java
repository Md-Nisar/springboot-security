package com.mna.springbootsecurity.payment.base.dto;

import com.mna.springbootsecurity.payment.base.enums.PaymentGatewayType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Data Transfer Object for initiating a payment.
 * Contains information required from the client to start a payment process.
 */
@Data
public class PaymentRequestData {

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "1.00", message = "Amount must be at least 1.00")
    private BigDecimal amount;

    @NotBlank(message = "Currency cannot be blank")
    @Size(min = 3, max = 3, message = "Currency code must be 3 characters long (e.g., INR, USD)")
    private String currency;

    @NotNull(message = "Payment gateway type cannot be null")
    private PaymentGatewayType gatewayType;

    // Optional fields, can be expanded based on requirements
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String description; // Description of the payment
}