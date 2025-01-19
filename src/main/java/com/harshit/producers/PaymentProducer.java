package com.harshit.producers;

import com.harshit.entity.Payment;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.MessageBody;
import jakarta.inject.Singleton;

import java.util.concurrent.ConcurrentHashMap;

@KafkaClient
@Singleton
public interface PaymentProducer {

    ConcurrentHashMap<String, Boolean> publishedPayments = new ConcurrentHashMap<>();

    @Topic("payment_topic")
    default void sendPayment(@KafkaKey String orderId, @MessageBody Payment payment) {
        if (publishedPayments.putIfAbsent(orderId, true) == null) {
            System.out.printf("Publishing Payment for Order ID: %s%n", orderId);
            publishPayment(orderId, payment);
        } else {
            System.out.printf("Duplicate Payment for Order ID: %s detected. Skipping publishing.%n", orderId);
        }
    }

    @Topic("payment_topic")
    void publishPayment(@KafkaKey String orderId, @MessageBody Payment payment);
}
