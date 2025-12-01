package com.mna.springbootsecurity.payment.domain.dao;

import com.mna.springbootsecurity.payment.domain.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link PaymentTransaction} entities.
 * Provides standard CRUD operations and custom query methods.
 */
@Repository
public interface PaymentTransactionDao extends JpaRepository<PaymentTransaction, Long> {

    /**
     * Finds a payment transaction by its gateway-specific order ID.
     *
     * @param orderId The order ID to search for.
     * @return An {@link Optional} containing the transaction if found, or empty otherwise.
     */
    Optional<PaymentTransaction> findByOrderId(String orderId);

    // You can add more custom query methods here as needed, for example:
    // List<PaymentTransaction> findByStatus(String status);
    // List<PaymentTransaction> findByGatewayAndStatus(String gateway, String status);
}
