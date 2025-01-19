package com.harshit.producers;

import com.harshit.entity.Order;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.MessageBody;
import jakarta.inject.Singleton;

import java.util.concurrent.ConcurrentHashMap;

@KafkaClient//Simplifies interaction with Kafka topics by abstracting low-level Kafka client code.
//Intended to be used on interfaces . You cannot directly use a class with @KafkaClient in Micronaut. This is a Micronaut-specific design choice.
//If you annotate a class, Micronaut would need to modify or override that class at compile time to add Kafka logic, which could conflict with
// existing code or cause runtime issues.
@Singleton
public interface OrderProducer {
    ConcurrentHashMap<String, Boolean> publishedOrders = new ConcurrentHashMap<>();

//    You can write methods with bodies in interfaces in Java if:
//      1.The method is default (can be overridden by implementing classes).
//      2.The method is static (belongs to the interface and cannot be overridden).
    @Topic("order_topic")  // sendOrder doesn't require this annotation here but the publishOrder it consist does.
    default void sendOrder(@KafkaKey String orderId, @MessageBody Order order) {
        if (publishedOrders.putIfAbsent(orderId, true) == null) {
            System.out.printf("Publishing Order ID: %s%n", orderId);
            publishOrder(orderId, order);
        } else {
            System.out.printf("Duplicate Order ID: %s detected. Skipping publishing.%n", orderId);
        }
    }

//This function ensure that the logic for actually sending the order to Kafka is cleanly separated from the logic for checking duplicates.
//  Q.  How does my code know that this specific function is used to send/publish order to Kafka?
//Ans. The way Micronaut understands that the publishOrder method is used to send the order to Kafka is due to the @Topic annotation that you’ve placed on the method.
    @Topic("order_topic")
    void publishOrder(@KafkaKey String orderId, @MessageBody Order order);
}
