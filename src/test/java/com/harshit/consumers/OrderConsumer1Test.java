package com.harshit.consumers;

import com.harshit.entity.Order;
import com.harshit.producers.DlqProducer;
import com.harshit.service.OrderService;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

@MicronautTest
public class OrderConsumer1Test {

    @Mock
    OrderService orderService;

    @Mock
    DlqProducer dlqProducer;

    OrderConsumer1 orderConsumer1;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        orderConsumer1 = new OrderConsumer1();
        orderConsumer1.orderService = orderService;
        orderConsumer1.dlqProducer = dlqProducer;
    }

    @Test
    void testReceiveOrder_SuccessfulProcessing() {
        String orderId = "order1";
        Order order = new Order(orderId, "Product1", 5);
        // Mocking orderService behavior
        doNothing().when(orderService).processOrder(order);

        orderConsumer1.receiveOrder(orderId, order);
        verify(orderService, times(1)).processOrder(order);
        verify(dlqProducer, never()).sendToDlq(any());
    }

    @Test
    void testReceiveOrder_FailureProcessing() {
        String orderId = "order2";
        Order order = new Order(orderId, "Product2", 10);
        String errorMessage = "Processing error";

        // Mocking orderService to throw exception
        doThrow(new RuntimeException(errorMessage)).when(orderService).processOrder(order);

        orderConsumer1.receiveOrder(orderId, order);
        verify(orderService, times(1)).processOrder(order);
        verify(dlqProducer, times(1)).sendToDlq(argThat(dlqMessage ->
                dlqMessage.getOrderId().equals(orderId) &&
                        dlqMessage.getOrder().equals(order) &&
                        dlqMessage.getErrorMessage().contains(errorMessage)
        ));
    }
}
