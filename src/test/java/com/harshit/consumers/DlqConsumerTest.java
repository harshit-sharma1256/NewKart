package com.harshit.consumers;

import com.harshit.entity.DlqMessage;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

@MicronautTest
public class DlqConsumerTest {

    DlqConsumer dlqConsumer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        dlqConsumer = spy(new DlqConsumer());
    }

    @Test
    void testReceiveFailedMessage_SuccessfulAlert() {
        // Arrange
        DlqMessage dlqMessage = new DlqMessage("order123", null, "Processing error");

        // Act
        dlqConsumer.receiveFailedMessage(dlqMessage);

        // Assert
        verify(dlqConsumer, times(1)).sendAlert(dlqMessage);
    }

    @Test
    void testReceiveFailedMessage_AlertFailure() {
        // Arrange
        DlqMessage dlqMessage = new DlqMessage("order456", null, "Another error");

        // Mocking sendAlert to throw an exception
        doThrow(new RuntimeException("Alert failed")).when(dlqConsumer).sendAlert(dlqMessage);

        // Act
        dlqConsumer.receiveFailedMessage(dlqMessage);

        // Assert
        verify(dlqConsumer, times(1)).sendAlert(dlqMessage);
    }
}
