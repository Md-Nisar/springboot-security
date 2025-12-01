package com.mna.springbootsecurity.payment.base.property;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for Razorpay.
 * Fetches values from application.yml under the 'application.payment.razorpay.test' prefix.
 * <p>
 * We are using a Java Record here for an immutable data carrier.
 */

@Component
@Validated // Ensures that validation annotations like @NotBlank are processed
@ConfigurationProperties(prefix = "application.payment.razorpay.test")
@Getter
@Setter
public class RazorpayConfigProperties {

    @NotBlank(message = "Razorpay Key ID must not be blank")
    private String keyId;

    @NotBlank(message = "Razorpay Key Secret must not be blank")
    private String secretKey;
}
