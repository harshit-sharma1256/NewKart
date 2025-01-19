package com.harshit.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DlqMessageTest {

    @Test
    void testDlqMessageConstructor() {
        // Arrange
        String orderId = "order1";
        Order order = new Order(orderId, "Product1", 5);
        String errorMessage = "Order processing failed";

        // Act
        DlqMessage dlqMessage = new DlqMessage(orderId, order, errorMessage);

        // Assert
        assertEquals(orderId, dlqMessage.getOrderId());
        assertEquals(order, dlqMessage.getOrder());
        assertEquals(errorMessage, dlqMessage.getErrorMessage());
    }
}
