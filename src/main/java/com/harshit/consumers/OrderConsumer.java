package com.harshit.consumers;

import com.harshit.entity.DlqMessage;
import com.harshit.entity.Order;
import com.harshit.producers.DlqProducer;
import com.harshit.service.OrderService;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@KafkaListener(
        groupId = "order-consumer-group"
) // Handle the Kafka consumer lifecycle itself : setting up the Kafka
// consumer configuration, subscribing to the topic, polling for messages
// ,deserializing the message, handling errors and invoking the receiveOrder method when a message is consumed.
@Singleton
public class OrderConsumer {

    @Inject
    OrderService orderService;

    @Inject
    DlqProducer dlqProducer;

    @Topic("order_topic")
    public void receiveOrder(@KafkaKey String orderId, Order order) {
        System.out.printf("Consumer Id 0 :Received and acknowledged Order ID: %s%n", orderId);
        try {
            orderService.processOrder(order);
        } catch (Exception e) {
            System.err.printf("Failed to process Order ID: %s. Error: %s%n", orderId, e.getMessage());
            DlqMessage dlqMessage = new DlqMessage(orderId, order, e.getMessage());
            dlqProducer.sendToDlq(dlqMessage);
        }
    }
}

