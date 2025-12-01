package com.mna.springbootsecurity.payment.controller;

import com.mna.springbootsecurity.payment.base.dto.PaymentRequestData;
import com.mna.springbootsecurity.payment.base.dto.PaymentResponseData;
import com.mna.springbootsecurity.payment.base.dto.PaymentTransactionStatusData;
import com.mna.springbootsecurity.payment.exception.ResourceNotFoundException;
import com.mna.springbootsecurity.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling payment initiation requests.
 * This controller is responsible for exposing endpoints related to starting a payment process.
 */
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Endpoint to initiate a new payment.
     * This method accepts a POST request with payment details and delegates
     * the payment initiation process to the PaymentService.
     *
     * @param request The payment request data from the client, including amount, currency, and gateway type.
     *                The @Valid annotation ensures that the incoming request body is validated
     *                against constraints defined in PaymentRequestData.
     * @return A ResponseEntity containing PaymentResponseData (e.g., gateway redirect URL, order ID)
     * and an appropriate HTTP status (e.g., 200 OK for success, 400 Bad Request for client errors).
     */
    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponseData> initiatePayment(@Valid @RequestBody PaymentRequestData request) {
        log.info("Received request to initiate payment for gateway: {}", request.getGatewayType());
        try {
            PaymentResponseData response = paymentService.initiatePayment(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("Failed to initiate payment: {}", e.getMessage(), e);
            // In a production application, you would typically use a global @ControllerAdvice
            // to handle exceptions and return a more structured error response DTO.
            // For simplicity, here we return a BAD_REQUEST status.
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Endpoint to retrieve the status of a payment transaction by its ID.
     * This method queries the internal database for the transaction status,
     * it does not call the payment gateway directly.
     *
     * @param transactionId The unique ID of the payment transaction.
     * @return A ResponseEntity containing PaymentTransactionStatusDTO with transaction details
     * and HTTP status 200 OK if found, or 404 Not Found if the transaction does not exist.
     */
    @GetMapping("/status/{transactionId}")
    public ResponseEntity<PaymentTransactionStatusData> getPaymentStatus(@PathVariable Long transactionId) {
        log.info("Received request to get status for transaction ID: {}", transactionId);
        try {
            PaymentTransactionStatusData statusData = paymentService.getPaymentStatus(transactionId);
            return new ResponseEntity<>(statusData, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            log.warn("Transaction with ID {} not found: {}", transactionId, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error retrieving status for transaction ID {}: {}", transactionId, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}