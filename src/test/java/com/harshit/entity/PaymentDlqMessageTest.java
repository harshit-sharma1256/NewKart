package com.harshit.entity;



import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentDlqMessageTest {

    @Test
    void testPaymentDlqMessageConstructor() {
        // Arrange
        String orderId = "order1";
        Payment payment = new Payment(orderId, 100.0);
        String errorMessage = "Insufficient funds";

        // Act
        PaymentDlqMessage dlqMessage = new PaymentDlqMessage(orderId, payment, errorMessage);

        // Assert
        assertEquals(orderId, dlqMessage.getOrderId());
        assertEquals(payment, dlqMessage.getPayment());
        assertEquals(errorMessage, dlqMessage.getErrorMessage());
    }
}
