package com.harshit.producers;

import com.harshit.entity.DlqMessage;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface DlqProducer {
    @Topic("order_dlq")
    void sendToDlq(DlqMessage dlqMessage);
}
