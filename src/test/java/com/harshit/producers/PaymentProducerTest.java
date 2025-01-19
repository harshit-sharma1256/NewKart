package com.harshit.producers;

import com.harshit.entity.Payment;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

@MicronautTest
public class PaymentProducerTest {

    @Inject
    PaymentProducer paymentProducer;

    private PaymentProducer spyPaymentProducer;

    @BeforeEach
    public void setup() {
        spyPaymentProducer = spy(paymentProducer);
    }

//    @Test
//    void testSendPayment_NewPayment() {
//        // Arrange
//        String orderId = "order1";
//        Payment payment = new Payment(orderId, 100.0);
//        doNothing().when(spyPaymentProducer).publishPayment(orderId, payment); // Mocking publishPayment
//
//        // Act
//        spyPaymentProducer.sendPayment(orderId, payment);  // This should internally call publishPayment
//
//        // Assert
//        verify(spyPaymentProducer, times(1)).sendPayment(orderId, payment); // Ensure sendPayment is called
//        verify(spyPaymentProducer, times(1)).publishPayment(orderId, payment); // Ensure publishPayment is called
//    }


    @Test
    void testSendPayment_DuplicatePayment() {
        // Arrange
        String orderId = "order1";
        Payment payment = new Payment(orderId, 100.0);
        PaymentProducer.publishedPayments.put(orderId, true); // Simulate the payment is already published

        // Act
        spyPaymentProducer.sendPayment(orderId, payment);

        // Assert
        verify(spyPaymentProducer, never()).publishPayment(orderId, payment);  // Ensure publishPayment is NOT called
    }

    @Test
    void testPublishPayment_CallsCorrectly() {
        // Arrange
        String orderId = "order2";
        Payment payment = new Payment(orderId, 100.0);
        doNothing().when(spyPaymentProducer).publishPayment(orderId, payment);

        // Act
        spyPaymentProducer.publishPayment(orderId, payment);

        // Assert
        verify(spyPaymentProducer, times(1)).publishPayment(orderId, payment);  // Ensure publishPayment is called
    }
}
