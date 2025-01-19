package com.harshit.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentEntityTest {

    @Test
    void testPaymentConstructor() {
        // Arrange
        String orderId = "order1";
        double amount = 100.0;

        // Act
        Payment payment = new Payment(orderId, amount);

        // Assert
        assertEquals(orderId, payment.getOrderId());
        assertEquals(amount, payment.getAmount());
    }
}
