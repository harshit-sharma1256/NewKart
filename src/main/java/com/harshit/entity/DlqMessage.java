package com.harshit.entity;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Introspected
@Serdeable
@Data
@AllArgsConstructor
public class DlqMessage {
    private String orderId;
    private Order order;
    private String errorMessage;


}
