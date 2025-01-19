package com.harshit.consumers;

import com.harshit.entity.Payment;
import com.harshit.entity.PaymentDlqMessage;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

@MicronautTest
public class PaymentDlqConsumerTest {

    PaymentDlqConsumer paymentDlqConsumer;

    @BeforeEach
    public void setup() {
        paymentDlqConsumer = spy(new PaymentDlqConsumer());
    }

    @Test
    void testReceiveFailedPayment_SuccessfulAlert() {
        // Arrange
        Payment payment = new Payment("order123", 100.0);
        PaymentDlqMessage dlqMessage = new PaymentDlqMessage("order123", payment, "Payment processing error");

        // Act
        paymentDlqConsumer.receiveFailedPayment(dlqMessage);

        // Assert
        verify(paymentDlqConsumer, times(1)).sendAlert(dlqMessage);
    }

    @Test
    void testReceiveFailedPayment_AlertFailure() {
        // Arrange
        Payment payment = new Payment("order456", 200.0);
        PaymentDlqMessage dlqMessage = new PaymentDlqMessage("order456", payment, "Critical error");

        // Mocking sendAlert to throw an exception
        doThrow(new RuntimeException("Alert sending failed")).when(paymentDlqConsumer).sendAlert(dlqMessage);

        // Act
        paymentDlqConsumer.receiveFailedPayment(dlqMessage);

        // Assert
        verify(paymentDlqConsumer, times(1)).sendAlert(dlqMessage);
    }
}
