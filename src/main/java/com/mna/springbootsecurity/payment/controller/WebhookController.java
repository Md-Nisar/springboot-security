package com.mna.springbootsecurity.payment.controller;

import com.mna.springbootsecurity.payment.base.dto.WebhookPayload;
import com.mna.springbootsecurity.payment.base.enums.PaymentGatewayType;
import com.mna.springbootsecurity.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling incoming webhook notifications from payment gateways.
 * This controller is specifically designed to receive and process asynchronous updates
 * about payment statuses.
 */
@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
@Slf4j
public class WebhookController {

    private final PaymentService paymentService;

    /**
     * Endpoint to handle incoming webhooks from payment gateways.
     * This method receives POST requests from payment gateways containing updates
     * about payment statuses (e.g., success, failure).
     *
     * @param gatewayType The type of payment gateway (e.g., RAZORPAY, STRIPE), extracted from the URL path.
     *                    Spring automatically converts the path variable string to the PaymentGatewayType enum.
     * @param payload     The raw webhook payload and signature sent by the payment gateway.
     *                    This payload is bound from the request body.
     * @return A ResponseEntity indicating the webhook was received successfully (200 OK).
     * It's common practice to return 200 OK to webhooks even if internal processing fails,
     * to prevent the gateway from retrying the webhook excessively. Errors should be logged internally.
     */
    @PostMapping("/{gatewayType}")
    public ResponseEntity<Void> handleWebhook(
            @PathVariable PaymentGatewayType gatewayType,
            @RequestBody WebhookPayload payload) {
        log.info("Received webhook for gateway: {}", gatewayType);
        try {
            paymentService.handleWebhook(gatewayType, payload);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error processing webhook for gateway {}: {}", gatewayType, e.getMessage(), e);
            // As per webhook best practices, still return OK to prevent retries,
            // unless the error is due to an unrecoverable issue with the request itself.
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
