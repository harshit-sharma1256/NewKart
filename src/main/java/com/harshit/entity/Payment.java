package com.harshit.entity;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Introspected
@Serdeable
@Data
@AllArgsConstructor
public class Payment {
    private String orderId;
    private double amount;

    public Payment() {
        this.orderId = orderId;
        this.amount = amount;
    }
}
