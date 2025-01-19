package com.harshit.consumers;

import com.harshit.entity.DlqMessage;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Singleton;

@KafkaListener(
        groupId = "dlq-consumer-group",
        offsetReset = OffsetReset.EARLIEST
)
@Singleton
public class DlqConsumer {

    @Topic("order_dlq")
    public void receiveFailedMessage(DlqMessage dlqMessage) {
        System.err.printf("Processing failed order from DLQ%n%s%n", dlqMessage);

        try {
            sendAlert(dlqMessage);
        } catch (Exception e) {
            System.err.printf("Failed to send alert for Order ID: %s. Error: %s%n", dlqMessage.getOrderId(), e.getMessage());
        }
    }

    void sendAlert(DlqMessage dlqMessage) {
        System.out.printf("ALERT: Issue with Order ID: %s%nOrder Details: %s%nError: %s%n",
                dlqMessage.getOrderId(), dlqMessage.getOrder(), dlqMessage.getErrorMessage());
    }
}
