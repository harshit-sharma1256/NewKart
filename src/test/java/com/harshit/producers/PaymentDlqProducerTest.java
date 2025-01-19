package com.harshit.producers;

import com.harshit.entity.PaymentDlqMessage;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

@MicronautTest
public class PaymentDlqProducerTest {

    @Mock
    PaymentDlqProducer paymentDlqProducer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendToDlq() {
        // Arrange
        String orderId = "order1";
        PaymentDlqMessage dlqMessage = new PaymentDlqMessage(orderId, null, "Error message");

        // Mocking the behavior of sendToDlq
        doNothing().when(paymentDlqProducer).sendToDlq(dlqMessage);

        // Act
        paymentDlqProducer.sendToDlq(dlqMessage);

        // Assert
        verify(paymentDlqProducer, times(1)).sendToDlq(dlqMessage);  // Ensure sendToDlq is called once
    }
}
