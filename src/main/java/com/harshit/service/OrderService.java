package com.harshit.service;

import com.harshit.entity.Order;
import com.harshit.producers.OrderProducer;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class OrderService {

    @Inject
    OrderProducer orderProducer;

    private final ConcurrentHashMap<String, Order> validOrders = new ConcurrentHashMap<>();

    public void processOrder(Order order) {
        System.out.println("Order processed locally: " + order);
        validOrders.put(order.getOrderId(), order);

        // Sending order event message to Kafka
        System.out.println("Sending order to Kafka: " + order);
        orderProducer.sendOrder(order.getOrderId(), order);
        System.out.println("Order sent to Kafka successfully.");
    }

    public boolean isOrderValid(String orderId) {
        return validOrders.containsKey(orderId);
    }
}
