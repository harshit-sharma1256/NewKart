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
public class PaymentConsumerTest {

    @Mock
    PaymentService paymentService;

    @Mock
    PaymentDlqProducer paymentDlqProducer;

    PaymentConsumer paymentConsumer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        paymentConsumer = new PaymentConsumer();
        paymentConsumer.paymentService = paymentService;
        paymentConsumer.paymentDlqProducer = paymentDlqProducer;
    }

    @Test
    void testReceivePayment_SuccessfulProcessing() {
        String orderId = "order1";
        Payment payment = new Payment(orderId, 500.00);
        doNothing().when(paymentService).processPayment(payment);
        paymentConsumer.receivePayment(orderId, payment);
        verify(paymentService, times(1)).processPayment(payment);
        verify(paymentDlqProducer, never()).sendToDlq(any());
    }

    @Test
    void testReceivePayment_FailureProcessing() {
        String orderId = "order2";
        Payment payment = new Payment(orderId, 1000.00);
        String errorMessage = "Insufficient funds";
        doThrow(new RuntimeException(errorMessage)).when(paymentService).processPayment(payment);
        paymentConsumer.receivePayment(orderId, payment);
        verify(paymentService, times(1)).processPayment(payment);
        verify(paymentDlqProducer, times(1)).sendToDlq(argThat(dlqMessage ->
                dlqMessage.getOrderId().equals(orderId) &&
                        dlqMessage.getPayment().equals(payment) &&
                        dlqMessage.getErrorMessage().contains(errorMessage)
        ));
    }
}
