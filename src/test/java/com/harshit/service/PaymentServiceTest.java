package com.harshit.service;

import com.harshit.entity.Payment;
import com.harshit.producers.PaymentProducer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

@MicronautTest
public class PaymentServiceTest {

    @Mock
    private PaymentProducer paymentProducer;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessPayment_Success() {
        Payment payment = new Payment("order123", 100.0);
        when(orderService.isOrderValid(payment.getOrderId())).thenReturn(true);
        paymentService.processPayment(payment);
        verify(paymentProducer, times(1)).sendPayment(payment.getOrderId(), payment);
    }

    @Test
    void testProcessPayment_OrderNotValid() {
        Payment payment = new Payment("order123", 100.0);
        when(orderService.isOrderValid(payment.getOrderId())).thenReturn(false);
        paymentService.processPayment(payment);
        verify(paymentProducer, never()).sendPayment(payment.getOrderId(), payment);
    }
}
