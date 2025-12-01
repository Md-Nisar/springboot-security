package com.mna.springbootsecurity.payment.base.property;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for Stripe.
 * Fetches values from application.yml under the 'application.payment.stripe.test' prefix.
 */
@Validated
@Component
@ConfigurationProperties(prefix = "application.payment.stripe.test")
@Getter
@Setter
public class StripeConfigProperties {

    @NotBlank(message = "Stripe Secret Key must not be blank")
    String publicKey;

    @NotBlank(message = "Stripe Public Key must not be blank")
    String secretKey;

    @NotBlank(message = "Webhook secret must not be blank")
    String webhookSecret;
}