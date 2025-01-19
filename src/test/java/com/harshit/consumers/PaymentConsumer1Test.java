package com.harshit.consumers;

import com.harshit.entity.Payment;
import com.harshit.producers.PaymentDlqProducer;
import com.harshit.service.PaymentService;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

@MicronautTest
public class PaymentConsumer1Test {

    @Mock
    PaymentService paymentService;

    @Mock
    PaymentDlqProducer paymentDlqProducer;

    PaymentConsumer1 paymentConsumer1;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        paymentConsumer1 = new PaymentConsumer1();
        paymentConsumer1.paymentService = paymentService;
        paymentConsumer1.paymentDlqProducer = paymentDlqProducer;
    }

    @Test
    void testReceivePayment_SuccessfulProcessing() {
        String orderId = "order3";
        Payment payment = new Payment(orderId, 750.00);
        doNothing().when(paymentService).processPayment(payment);
        paymentConsumer1.receivePayment(orderId, payment);
        verify(paymentService, times(1)).processPayment(payment);
        verify(paymentDlqProducer, never()).sendToDlq(any());
    }

    @Test
    void testReceivePayment_FailureProcessing() {
        String orderId = "order4";
        Payment payment = new Payment(orderId, 1500.00);
        String errorMessage = "Processing error";
        doThrow(new RuntimeException(errorMessage)).when(paymentService).processPayment(payment);
        paymentConsumer1.receivePayment(orderId, payment);
        verify(paymentService, times(1)).processPayment(payment);
        verify(paymentDlqProducer, times(1)).sendToDlq(argThat(dlqMessage ->
                dlqMessage.getOrderId().equals(orderId) &&
                        dlqMessage.getPayment().equals(payment) &&
                        dlqMessage.getErrorMessage().contains(errorMessage)
        ));
    }
}
