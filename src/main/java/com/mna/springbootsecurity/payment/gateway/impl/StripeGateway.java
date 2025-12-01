package com.mna.springbootsecurity.payment.gateway.impl;

import com.mna.springbootsecurity.payment.base.dto.PaymentRequestData;
import com.mna.springbootsecurity.payment.base.dto.PaymentResponseData;
import com.mna.springbootsecurity.payment.base.dto.WebhookPayload;
import com.mna.springbootsecurity.payment.base.enums.PaymentGatewayType;
import com.mna.springbootsecurity.payment.base.property.StripeConfigProperties;
import com.mna.springbootsecurity.payment.gateway.PaymentGateway;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StripeGateway implements PaymentGateway {

    private final StripeConfigProperties stripeConfigProperties;

    @Override
    public PaymentResponseData createOrder(PaymentRequestData request) {
        try {
            // Stripe also expects amount in the smallest currency unit
            long amountInCents = request.getAmount().longValue() * 100;

            PaymentIntentCreateParams params =
                    PaymentIntentCreateParams.builder()
                            .setAmount(amountInCents)
                            .setCurrency(request.getCurrency())
                            // In the latest version of the API, specifying the `automatic_payment_methods` parameter is optional
                            // because Stripe enables its functionality by default.
                            .setAutomaticPaymentMethods(
                                    PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build())
                            .build();

            log.info("Creating Stripe PaymentIntent with payload: {}", params);
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            log.info("Stripe PaymentIntent created successfully: {}", paymentIntent.getId());

            // Return a response containing the client secret
            return PaymentResponseData.builder()
                    .orderId(paymentIntent.getId()) // The PaymentIntent ID
                    .status(paymentIntent.getStatus())
                    .clientSecret(paymentIntent.getClientSecret()) // Essential for the frontend
                    .apiKey(stripeConfigProperties.getPublicKey()) // Send the public key
                    .build();

        } catch (StripeException e) {
            log.error("Error creating Stripe PaymentIntent", e);
            throw new RuntimeException("Could not create Stripe PaymentIntent.", e);
        }
    }

    @Override
    public boolean verifySignature(WebhookPayload payload) {
        try {
            // The signature is expected to be in the 'Stripe-Signature' header
            Webhook.constructEvent(
                    payload.getRawPayload(),
                    payload.getSignature(),
                    stripeConfigProperties.getWebhookSecret()
            );
            // If no exception is thrown, the signature is valid.
            log.info("Stripe webhook signature verified successfully.");
            return true;
        } catch (SignatureVerificationException e) {
            log.warn("Stripe webhook signature verification failed.", e);
            return false;
        } catch (Exception e) {
            log.error("Error during Stripe webhook processing.", e);
            return false;
        }
    }

    @Override
    public PaymentGatewayType getType() {
        return PaymentGatewayType.STRIPE;
    }
}
