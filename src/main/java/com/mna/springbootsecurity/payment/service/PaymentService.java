package com.mna.springbootsecurity.payment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mna.springbootsecurity.payment.base.dto.PaymentRequestData;
import com.mna.springbootsecurity.payment.base.dto.PaymentResponseData;
import com.mna.springbootsecurity.payment.base.dto.PaymentTransactionStatusData;
import com.mna.springbootsecurity.payment.base.dto.WebhookPayload;
import com.mna.springbootsecurity.payment.base.enums.PaymentGatewayType;
import com.mna.springbootsecurity.payment.domain.dao.PaymentTransactionDao;
import com.mna.springbootsecurity.payment.domain.entity.PaymentTransaction;
import com.mna.springbootsecurity.payment.exception.ResourceNotFoundException;
import com.mna.springbootsecurity.payment.factory.PaymentGatewayFactory;
import com.mna.springbootsecurity.payment.gateway.PaymentGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The primary service for handling all payment-related operations.
 * It orchestrates the payment flow by coordinating between the gateway factory,
 * database repository, and specific gateway implementations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentGatewayFactory gatewayFactory;
    private final PaymentTransactionDao transactionRepository;
    private final ObjectMapper objectMapper; // For parsing webhook JSON

    /**
     * Initiates a payment by selecting the correct gateway, creating a local transaction record,
     * and calling the gateway to create a payment order.
     *
     * @param request The client's request to start a payment.
     * @return A {@link PaymentResponseData} containing details for the frontend.
     */
    @Transactional // Ensures that database operations are atomic
    public PaymentResponseData initiatePayment(PaymentRequestData request) {
        log.info("Initiating payment for amount {} {} via {}", request.getAmount(), request.getCurrency(), request.getGatewayType());

        // 1. Get the correct gateway implementation from the factory
        PaymentGateway gateway = gatewayFactory.getGateway(request.getGatewayType());

        // 2. Create and save an initial transaction record in our database
        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency());
        transaction.setGateway(request.getGatewayType().name());
        transaction.setStatus("CREATED");
        transaction = transactionRepository.save(transaction);
        log.info("Saved initial transaction record with ID: {}", transaction.getId());

        try {
            // 3. Call the gateway to create the order
            PaymentResponseData gatewayResponse = gateway.createOrder(request);

            // 4. Update our transaction record with the gateway's order ID
            transaction.setOrderId(gatewayResponse.getOrderId());
            transactionRepository.save(transaction);
            log.info("Updated transaction record with gateway Order ID: {}", gatewayResponse.getOrderId());

            return gatewayResponse;

        } catch (Exception e) {
            // If gateway call fails, mark our transaction as FAILED
            log.error("Payment initiation failed for transaction ID: {}", transaction.getId(), e);
            transaction.setStatus("FAILED");
            transaction.setDetails(e.getMessage());
            transactionRepository.save(transaction);
            // Propagate exception to be handled by a controller advisor
            throw new RuntimeException("Payment initiation failed.", e);
        }
    }

    /**
     * Handles incoming webhooks from payment gateways.
     * Verifies the webhook's authenticity and updates the transaction status accordingly.
     *
     * @param gatewayType The type of gateway sending the webhook.
     * @param payload     The webhook payload containing raw data and signature.
     */
    @Transactional
    public void handleWebhook(PaymentGatewayType gatewayType, WebhookPayload payload) {
        log.info("Handling webhook for gateway: {}", gatewayType);
        PaymentGateway gateway = gatewayFactory.getGateway(gatewayType);

        // 1. Verify the webhook signature
        if (!gateway.verifySignature(payload)) {
            log.warn("Webhook signature verification failed for gateway: {}", gatewayType);
            // In production, you might want to alert on this, as it could be a fraudulent attempt
            return;
        }

        // 2. Parse the payload to get the order ID and determine the event outcome
        try {
            JsonNode root = objectMapper.readTree(payload.getRawPayload());
            String orderId;
            boolean isSuccess;

            // This parsing logic is gateway-specific.
            if (gatewayType == PaymentGatewayType.RAZORPAY) {
                orderId = root.at("/payload/payment/entity/order_id").asText();
                String event = root.at("/event").asText();
                isSuccess = "payment.captured".equals(event);
            } else if (gatewayType == PaymentGatewayType.STRIPE) {
                orderId = root.at("/data/object/id").asText();
                String event = root.at("/type").asText();
                isSuccess = "payment_intent.succeeded".equals(event);
            } else {
                log.error("Webhook handling not implemented for gateway: {}", gatewayType);
                return;
            }

            if (orderId == null || orderId.isEmpty()) {
                log.error("Could not extract order ID from webhook for gateway: {}", gatewayType);
                return;
            }

            log.info("Processing webhook for Order ID: {} and status: {}", orderId, isSuccess ? "SUCCESS" : "FAILURE");

            // 3. Find the transaction in our database and update its status
            transactionRepository.findByOrderId(orderId).ifPresentOrElse(
                    transaction -> {
                        if (isSuccess) {
                            transaction.setStatus("SUCCESS");
                            // You can also extract and save the payment ID (e.g., from Razorpay: /payload/payment/entity/id)
                            log.info("Transaction {} marked as SUCCESS.", transaction.getId());
                        } else {
                            transaction.setStatus("FAILED");
                            log.info("Transaction {} marked as FAILED.", transaction.getId());
                        }
                        transactionRepository.save(transaction);
                    },
                    () -> log.error("Received webhook for an unknown transaction with Order ID: {}", orderId)
            );

        } catch (Exception e) {
            log.error("Error processing webhook for gateway: {}", gatewayType, e);
        }
    }

    /**
     * Retrieves the status of a payment transaction from the database.
     *
     * @param transactionId The ID of the transaction to retrieve.
     * @return A DTO containing the status and relevant details of the transaction.
     * @throws ResourceNotFoundException if no transaction with the given ID is found.
     */
    @Transactional(readOnly = true)
    public PaymentTransactionStatusData getPaymentStatus(Long transactionId) {
        log.info("Fetching status for transaction ID: {}", transactionId);
        PaymentTransaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment transaction with ID " + transactionId + " not found."));

        return mapToPaymentTransactionStatusDTO(transaction);
    }

    /**
     * Helper method to map a PaymentTransaction entity to a PaymentTransactionStatusData.
     *
     * @param transaction The PaymentTransaction entity.
     * @return The mapped PaymentTransactionStatusData.
     */
    private PaymentTransactionStatusData mapToPaymentTransactionStatusDTO(PaymentTransaction transaction) {
        return PaymentTransactionStatusData.builder()
                .transactionId(transaction.getId())
                .status(transaction.getStatus())
                .orderId(transaction.getOrderId())
                .gateway(transaction.getGateway())
                // Add other fields as needed from the transaction entity to the DTO
                .build();
    }
}
