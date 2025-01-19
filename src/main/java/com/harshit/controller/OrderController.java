package com.harshit.controller;

import com.harshit.entity.Order;
import com.harshit.service.OrderService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

@Controller(value = "/orders")
@Tag(name = "Order Processing", description = "APIs related to managing orders")
public class OrderController {

    @Inject
    OrderService orderService;

    @Operation(summary = "Check the health of the Order API", description = "Returns a simple status message")
    @Get(value = "/health", produces = APPLICATION_JSON)
    public String health() {
        return "OK";
    }

    @Operation(summary = "Creating an order", description = "Creates an order and sends message to Kafka")
    @Post("/create")
    public HttpResponse<String> createOrder(@Body Order order) {
        if (order.getOrderId() == null || order.getOrderId().trim().isEmpty()) {
            return HttpResponse.badRequest("Invalid orderId: Must be a non-null string");
        }
        if (order.getProduct() == null || order.getProduct().trim().isEmpty()) {
            return HttpResponse.badRequest("Invalid product: Must be a non-null string");
        }
        if (order.getQuantity() < 1) {
            return HttpResponse.badRequest("Invalid quantity: Must be greater than or equal to 1");
        }
        orderService.processOrder(order);
        return HttpResponse.ok("Order created and sent to Kafka: " + order);
    }
}
