package com.harshit.consumers;

import com.harshit.entity.Payment;
import com.harshit.entity.PaymentDlqMessage;
import com.harshit.producers.PaymentDlqProducer;
import com.harshit.service.PaymentService;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@KafkaListener(groupId = "payment-consumer-group")
@Singleton
public class PaymentConsumer {

    @Inject
    PaymentService paymentService;

    @Inject
    PaymentDlqProducer paymentDlqProducer;

    @Topic("payment_topic")
    public void receivePayment(@KafkaKey String orderId, Payment payment) {
        System.out.printf("P-Consumer Id 0 :Received and acknowledged Order ID: %s%n", orderId);
        try {
            paymentService.processPayment(payment);
        } catch (Exception e) {
            System.err.println("Failed to process payment, sending to DLQ: " + orderId);
            PaymentDlqMessage dlqMessage = new PaymentDlqMessage(orderId, payment, e.getMessage());
           paymentDlqProducer.sendToDlq(dlqMessage);
        }
    }
}

