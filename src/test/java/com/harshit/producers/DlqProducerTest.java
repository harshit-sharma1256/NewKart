package com.harshit.producers;

import com.harshit.entity.DlqMessage;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

@MicronautTest
public class DlqProducerTest {

    @Mock
    DlqProducer dlqProducer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendToDlq() {
        // Arrange
        String orderId = "order1";
        DlqMessage dlqMessage = new DlqMessage(orderId, null, "Error message");

        // Mocking the behavior of sendToDlq
        doNothing().when(dlqProducer).sendToDlq(dlqMessage);

        // Act
        dlqProducer.sendToDlq(dlqMessage);

        // Assert
        verify(dlqProducer, times(1)).sendToDlq(dlqMessage);  // Ensure sendToDlq is called once
    }
}
