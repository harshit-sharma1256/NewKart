package com.harshit.entity;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Introspected //used to optimize reflection-based operations (e.g., accessing properties of a class).
@Serdeable // integrates with Micronaut’s native serialization system for improved performance AND generates serializer and deserializer code for class.
@Data
@AllArgsConstructor
public class Order {

    private String orderId;
    private String product;
    private int quantity;

}
