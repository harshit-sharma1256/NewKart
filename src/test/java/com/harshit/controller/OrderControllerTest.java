package com.harshit.controller;

import com.harshit.entity.Order;
import com.harshit.service.OrderService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@MicronautTest
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHealth() {
        String response = orderController.health();
        assertEquals("OK", response);
    }

    @Test
    void testCreateOrder_ValidOrder() {
        Order order = new Order("order123", "itemABC", 2);
        doNothing().when(orderService).processOrder(order);
        HttpResponse<String> response = orderController.createOrder(order);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertTrue(response.body().contains("Order created and sent to Kafka"));
    }

    @Test
    void testCreateOrder_InvalidOrderId() {
        Order order = new Order("", "itemABC", 2);
        HttpResponse<String> response = orderController.createOrder(order);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertTrue(response.body().contains("Invalid orderId"));
    }

    @Test
    void testCreateOrder_InvalidProduct() {
        Order order = new Order("order123", "", 2);
        HttpResponse<String> response = orderController.createOrder(order);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertTrue(response.body().contains("Invalid product"));
    }

    @Test
    void testCreateOrder_InvalidQuantity() {
        Order order = new Order("order123", "itemABC", 0);
        HttpResponse<String> response = orderController.createOrder(order);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertTrue(response.body().contains("Invalid quantity"));
    }
}
