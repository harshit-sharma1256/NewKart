package com.harshit.service;

import com.harshit.entity.Order;
import com.harshit.producers.OrderProducer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MicronautTest
public class OrderServiceTest {

    @Mock
    private OrderProducer orderProducer;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setup() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessOrder() {
        Order order = new Order("order123", "itemABC", 2);
        orderService.processOrder(order);
        assertTrue(orderService.isOrderValid(order.getOrderId()));
        verify(orderProducer, times(1)).sendOrder(order.getOrderId(), order);
    }

    @Test
    void testIsOrderValid_ReturnsTrueForValidOrder() {
        Order order = new Order("order456", "itemXYZ", 1);
        orderService.processOrder(order);
        boolean result = orderService.isOrderValid("order456");
        assertTrue(result);
    }

    @Test
    void testIsOrderValid_ReturnsFalseForInvalidOrder() {
        boolean result = orderService.isOrderValid("invalidOrder");
        assertFalse(result);
    }
}
