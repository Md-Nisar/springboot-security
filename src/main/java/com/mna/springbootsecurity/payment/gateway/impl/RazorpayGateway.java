package com.mna.springbootsecurity.payment.gateway.impl;

import com.mna.springbootsecurity.payment.base.dto.PaymentRequestData;
import com.mna.springbootsecurity.payment.base.dto.PaymentResponseData;
import com.mna.springbootsecurity.payment.base.dto.WebhookPayload;
import com.mna.springbootsecurity.payment.base.enums.PaymentGatewayType;
import com.mna.springbootsecurity.payment.base.property.RazorpayConfigProperties;
import com.mna.springbootsecurity.payment.gateway.PaymentGateway;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
@Slf4j // Lombok: for logging
public class RazorpayGateway implements PaymentGateway {

    private final RazorpayClient razorpayClient;
    private final RazorpayConfigProperties razorpayConfigProperties;

    @Override
    public PaymentResponseData createOrder(PaymentRequestData request) {
        try {
            // Razorpay expects the amount in the smallest currency unit (e.g., paise for INR)
            BigInteger amountInPaise = BigInteger.valueOf(request.getAmount().longValue() * 100);

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", request.getCurrency());
            orderRequest.put("receipt", "receipt_order_" + System.currentTimeMillis());

            log.info("Creating Razorpay order with payload: {}", orderRequest);
            Order order = razorpayClient.orders.create(orderRequest);
            log.info("Razorpay order created successfully: {}", order);

            String orderId = order.get("id");

            // Return a response that the frontend can use
            return PaymentResponseData.builder()
                    .orderId(orderId)
                    .status(order.get("status"))
                    .apiKey(razorpayConfigProperties.getKeyId()) // Send the public key to the client
                    .build();

        } catch (RazorpayException e) {
            log.error("Error creating Razorpay order", e);
            // In a real app, throw a custom, user-friendly exception
            throw new RuntimeException("Could not create Razorpay order.", e);
        }
    }

    @Override
    public boolean verifySignature(WebhookPayload payload) {
        try {
            // The webhook payload from Razorpay contains 'order_id', 'payment_id'
            // We need to construct the expected signature string from these.
            // The rawPayload of the webhook is needed for verification.

            JSONObject payloadJson = new JSONObject(payload.getRawPayload());
            String orderId = payloadJson.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity").getString("order_id");
            String paymentId = payloadJson.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity").getString("id");

            String expectedSignature = orderId + "|" + paymentId;
            JSONObject json = new JSONObject(expectedSignature);

            return Utils.verifyPaymentSignature(json, razorpayConfigProperties.getSecretKey());

        } catch (RazorpayException e) {
            log.error("Razorpay signature verification failed", e);
            return false;
        } catch (Exception e) {
            log.error("Error parsing Razorpay webhook payload for signature verification", e);
            return false;
        }
    }

    @Override
    public PaymentGatewayType getType() {
        return PaymentGatewayType.RAZORPAY;
    }
}