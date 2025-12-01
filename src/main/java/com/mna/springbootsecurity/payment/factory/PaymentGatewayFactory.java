package com.mna.springbootsecurity.payment.factory;

import com.mna.springbootsecurity.payment.base.enums.PaymentGatewayType;
import com.mna.springbootsecurity.payment.gateway.PaymentGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A factory component for retrieving the correct payment gateway implementation.
 * This class is central to the Strategy Pattern, allowing for dynamic selection of the payment processing strategy.
 */
@Component
public class PaymentGatewayFactory {

    private final Map<PaymentGatewayType, PaymentGateway> gatewayMap;

    /**
     * Constructor-based dependency injection.
     * Spring will automatically inject a list of all beans that implement the PaymentGateway interface.
     *
     * @param gateways The list of all available PaymentGateway beans.
     */
    @Autowired
    public PaymentGatewayFactory(List<PaymentGateway> gateways) {
        // First, collect to a modifiable EnumMap
        Map<PaymentGatewayType, PaymentGateway> tempGatewayMap = gateways.stream()
                .collect(Collectors.toMap(
                        PaymentGateway::getType,
                        Function.identity(),
                        (existing, replacement) -> existing, // In case of duplicate keys, keep the existing one
                        () -> new EnumMap<>(PaymentGatewayType.class)
                ));

        // Then, wrap it in an unmodifiable map
        this.gatewayMap = Collections.unmodifiableMap(tempGatewayMap);
    }


    /**
     * Retrieves a payment gateway implementation based on its type.
     *
     * @param type The {@link PaymentGatewayType} enum constant representing the desired gateway.
     * @return The corresponding {@link PaymentGateway} implementation.
     * @throws IllegalArgumentException if no gateway is found for the given type.
     */
    public PaymentGateway getGateway(PaymentGatewayType type) {
        PaymentGateway gateway = gatewayMap.get(type);
        if (gateway == null) {
            throw new IllegalArgumentException("Unsupported payment gateway type: " + type);
        }
        return gateway;
    }
}
