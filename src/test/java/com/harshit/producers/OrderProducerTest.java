package com.harshit.producers;

import com.harshit.entity.Order;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

@MicronautTest
public class OrderProducerTest {

    @Inject
    OrderProducer orderProducer;

    private OrderProducer spyOrderProducer;

    @BeforeEach
    public void setup() {
        spyOrderProducer = spy(orderProducer);
    }

    @Test
    void testSendOrder_NewOrder() {
        String orderId = "order1";
        Order order = new Order(orderId, "Product1", 5);
        // Stub the behavior of publishOrder to do nothing (mocking the actual Kafka call)
        doNothing().when(spyOrderProducer).publishOrder(orderId, order);

        spyOrderProducer.sendOrder(orderId, order);
        verify(spyOrderProducer, times(1)).publishOrder(orderId, order); // Ensure publishOrder is called
    }

    @Test
    void testSendOrder_DuplicateOrder() {
        String orderId = "order1";
        Order order = new Order(orderId, "Product1", 5);
        // Simulate the order as already published
        OrderProducer.publishedOrders.put(orderId, true);
        spyOrderProducer.sendOrder(orderId, order);
        verify(spyOrderProducer, never()).publishOrder(orderId, order); // Ensure publishOrder is NOT called
    }

    @Test
    void testPublishOrder_CallsCorrectly() {
        String orderId = "order2";
        Order order = new Order(orderId, "Product2", 10);
        doNothing().when(spyOrderProducer).publishOrder(orderId, order);
        spyOrderProducer.publishOrder(orderId, order);
        verify(spyOrderProducer, times(1)).publishOrder(orderId, order); // Ensure publishOrder is called
    }
}
