package com.mna.springbootsecurity.payment.config;

import com.mna.springbootsecurity.payment.base.property.RazorpayConfigProperties;
import com.mna.springbootsecurity.payment.base.property.StripeConfigProperties;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to set up and provide beans for payment gateway SDK clients.
 */
@Configuration
@RequiredArgsConstructor
public class PaymentGatewayConfig {

    private final RazorpayConfigProperties razorpayConfigProperties;
    private final StripeConfigProperties stripeConfigProperties;

    /**
     * Creates and configures the RazorpayClient bean.
     *
     * @return An instance of {@link RazorpayClient}.
     * @throws RazorpayException if the key ID or secret is invalid.
     */
    @Bean
    public RazorpayClient razorpayClient() throws RazorpayException {
        return new RazorpayClient(razorpayConfigProperties.getKeyId(), razorpayConfigProperties.getSecretKey());
    }

    /**
     * Initializes the static Stripe API key.
     * The Stripe SDK for Java uses a static variable for the API key.
     * This method runs after the bean has been constructed to set this key.
     */
    @PostConstruct
    public void initializeStripe() {
        Stripe.apiKey = stripeConfigProperties.getSecretKey();
    }
}