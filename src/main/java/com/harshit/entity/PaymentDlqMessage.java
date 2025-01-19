package com.harshit.entity;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Introspected
@Serdeable
@Data
@AllArgsConstructor
public class PaymentDlqMessage {
    private String orderId;
    private Payment payment;
    private String errorMessage;
}
