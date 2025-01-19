package com.harshit.entity;

import io.micronaut.core.beans.BeanIntrospection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderTest {

    @Test
    void testOrderEntity() {
        // Create an Order object
        Order order = new Order("123", "Laptop", 2);

        // Test getters
        assertEquals("123", order.getOrderId());
        assertEquals("Laptop", order.getProduct());
        assertEquals(2, order.getQuantity());

        // Test setters
        order.setOrderId("456");
        order.setProduct("Phone");
        order.setQuantity(1);

        assertEquals("456", order.getOrderId());
        assertEquals("Phone", order.getProduct());
        assertEquals(1, order.getQuantity());
    }

    @Test
    void testOrderIntrospection() {
        // Test if the Order class is properly introspected
        BeanIntrospection<Order> introspection = BeanIntrospection.getIntrospection(Order.class);
        assertNotNull(introspection);

        // Test introspection properties
        Order order = introspection.instantiate("123", "Laptop", 2);
        assertEquals("123", order.getOrderId());
        assertEquals("Laptop", order.getProduct());
        assertEquals(2, order.getQuantity());
    }
}
