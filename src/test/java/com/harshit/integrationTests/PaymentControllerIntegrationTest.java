package com.harshit.integrationTests;

import com.harshit.controller.PaymentController;
import com.harshit.entity.Payment;
import com.harshit.service.OrderService;
import com.harshit.service.PaymentService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@MicronautTest // Annotation used to indicate that the test class should run in the
// application context(a container that holds and manages the lifecycle of all beans and their dependencies
// in your application).
public class PaymentControllerIntegrationTest {

    @Inject //Used to inject dependencies (beans) from the application context into a class.
    @Client("/")
    HttpClient client;

    @Mock //used to create a "mock object" of a dependency that the class under test
    // interacts with.
    OrderService orderService;

    @Mock
    private PaymentService paymentService;

    @InjectMocks //used to create an instance of the "class under test" and automatically
    // inject its mocked dependencies .
    private PaymentController paymentController;

    private Payment validPayment;


    @BeforeEach //JUnit 5 annotation used to indicate that the annotated method should be executed before each test case in the test class.
    void setup() {
        MockitoAnnotations.openMocks(this); // Used to initialize the Mocks
        validPayment = new Payment("order123", 100.0);
    }

    @Test
    void testHealthEndpoint() {
        HttpRequest<Object> request = HttpRequest.GET("payments/health");
        HttpResponse<String> response = client.toBlocking().exchange(request, String.class);
        assertEquals(200, response.code());
        assertEquals("OK", response.body());
    }

    @Test
    void testCreatePayment_ValidPayment() {
        when(orderService.isOrderValid(validPayment.getOrderId())).thenReturn(true);
        HttpResponse<String> response = paymentController.createPayment(validPayment);
        assertEquals(200, response.code());
        assertTrue(response.body().contains("Payment created and sent to Kafka"));
        verify(paymentService, times(1)).processPayment(validPayment);
    }

    @Test
    void testCreatePayment_NonExistentOrder() {
        when(orderService.isOrderValid("invalidOrder")).thenReturn(false);
        Payment payment = new Payment("invalidOrder", 100.0);

        HttpResponse<String> response = paymentController.createPayment(payment);
        assertEquals(400, response.code());
        assertTrue(response.body().contains("does not exist"));
    }

    @Test
    void testCreatePayment_PaymentProcessingFailure() {
        when(orderService.isOrderValid("order123")).thenReturn(true);
        doThrow(new RuntimeException("Processing failed")).when(paymentService).processPayment(validPayment);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> paymentController.createPayment(validPayment));
        assertEquals("Processing failed", exception.getMessage());
    }

    @Test
    void testCreatePayment_LargeAmount() {
        Payment largePayment = new Payment("order123", 1_000_000.0);
        when(orderService.isOrderValid(largePayment.getOrderId())).thenReturn(true);

        HttpResponse<String> response = paymentController.createPayment(largePayment);
        assertEquals(200, response.code());
        assertTrue(response.body().contains("Payment created and sent to Kafka"));
    }



}



