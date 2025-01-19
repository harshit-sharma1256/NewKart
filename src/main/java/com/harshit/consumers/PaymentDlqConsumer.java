package com.harshit.consumers;

import com.harshit.entity.PaymentDlqMessage;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Singleton;

@KafkaListener(
        groupId = "payment-dlq-consumer-group",
        offsetReset = OffsetReset.EARLIEST
)
@Singleton
public class PaymentDlqConsumer {

    @Topic("payment_dlq")
    public void receiveFailedPayment(PaymentDlqMessage dlqMessage) {
        System.err.printf("Processing failed payment from DLQ%n%s%n", dlqMessage);

        try {
            sendAlert(dlqMessage);
        } catch (Exception e) {
            System.err.printf("Failed to send alert for Payment ID: %s. Error: %s%n", dlqMessage.getOrderId(), e.getMessage());
        }
    }

    void sendAlert(PaymentDlqMessage dlqMessage) {
        System.out.printf("ALERT: Issue with Payment for Order ID: %s%nPayment Details: %s%nError: %s%n",
                dlqMessage.getOrderId(), dlqMessage.getPayment(), dlqMessage.getErrorMessage());
    }
}
