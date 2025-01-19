package com.harshit.controller;

import com.harshit.entity.Payment;
import com.harshit.service.OrderService;
import com.harshit.service.PaymentService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

@Controller(value = "/payments")
@Tag(name = "Payment Processing", description = "APIs related to payment processing")
public class PaymentController {

    @Inject
    PaymentService paymentService;

    @Inject
    OrderService orderService;

    @Operation(summary = "Check the health of the Payment API", description = "Returns a simple status message")
    @Get(value = "/health", produces = APPLICATION_JSON)
    public String health() {
        return "OK";
    }

    @Operation(summary = "Creating a payment", description = "Creates a payment and sends message to Kafka")
    @Post("/create")
    public HttpResponse<String> createPayment(@Body Payment payment) {
        if (payment.getOrderId() == null || payment.getOrderId().trim().isEmpty()) {
            return HttpResponse.badRequest("Invalid orderId: Must be a non-null string");
        }
        if (payment.getAmount() <= 0) {
            return HttpResponse.badRequest("Invalid amount: Must be greater than 0");
        }
        boolean isOrderValid = orderService.isOrderValid(payment.getOrderId());
        if (!isOrderValid) {
            String errorMessage = "Cannot process payment. Order ID " + payment.getOrderId() + " does not exist.";
            return HttpResponse.badRequest(errorMessage);
        }
        paymentService.processPayment(payment);
        return HttpResponse.ok("Payment created and sent to Kafka: " + payment);
    }
}
