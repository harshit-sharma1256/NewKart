package com.harshit.controller;

import com.harshit.entity.Payment;
import com.harshit.service.OrderService;
import com.harshit.service.PaymentService;
import io.micronaut.http.HttpResponse;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private PaymentController paymentController;

    private Payment validPayment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validPayment = new Payment("order123", 100.0);
    }

    @Test
    void testHealth() {
        String response = paymentController.health();
        assertEquals("OK", response);
    }

    @Test
    void testCreatePayment_InvalidOrderId() {
        Payment invalidPayment = new Payment("", 100.0);
        HttpResponse<String> response = paymentController.createPayment(invalidPayment);
        assertEquals(400, response.code());
        assertTrue(response.body().contains("Invalid orderId"));
    }

    @Test
    void testCreatePayment_InvalidAmount() {
        Payment invalidPayment = new Payment("order123", -10.0);
        HttpResponse<String> response = paymentController.createPayment(invalidPayment);
        assertEquals(400, response.code());
        assertTrue(response.body().contains("Invalid amount"));
    }

    @Test
    void testCreatePayment_OrderNotValid() {
        when(orderService.isOrderValid(validPayment.getOrderId())).thenReturn(false);
        HttpResponse<String> response = paymentController.createPayment(validPayment);
        assertEquals(400, response.code());
        assertTrue(response.body().contains("Cannot process payment"));
    }

    @Test
    void testCreatePayment_Success() {
        when(orderService.isOrderValid(validPayment.getOrderId())).thenReturn(true);
        HttpResponse<String> response = paymentController.createPayment(validPayment);
        assertEquals(200, response.code());
        assertTrue(response.body().contains("Payment created and sent to Kafka"));
        verify(paymentService, times(1)).processPayment(validPayment);
    }
}
