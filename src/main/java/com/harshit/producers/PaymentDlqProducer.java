package com.harshit.producers;

import com.harshit.entity.PaymentDlqMessage;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface PaymentDlqProducer {
    @Topic("payment_dlq")
    void sendToDlq(PaymentDlqMessage dlqMessage);
}
