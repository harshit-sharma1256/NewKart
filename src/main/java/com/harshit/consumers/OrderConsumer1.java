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
)
@Singleton
public class OrderConsumer1 {

    @Inject
    OrderService orderService;

    @Inject
    DlqProducer dlqProducer;

    @Topic("order_topic")
    public void receiveOrder(@KafkaKey String orderId, Order order) {
        System.out.printf("Consumer Id 1 : Received and acknowledged Order ID: %s%n", orderId);
        try {
            orderService.processOrder(order);
        } catch (Exception e) {
            System.err.printf("Failed to process Order ID: %s. Error: %s%n", orderId, e.getMessage());
            DlqMessage dlqMessage = new DlqMessage(orderId, order, e.getMessage());
            dlqProducer.sendToDlq(dlqMessage);
        }
    }
}
