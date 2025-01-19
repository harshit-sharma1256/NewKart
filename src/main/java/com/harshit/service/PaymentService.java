package com.harshit.service;

import com.harshit.entity.Payment;
import com.harshit.producers.PaymentProducer;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class PaymentService {

    @Inject
    PaymentProducer paymentProducer;

    @Inject
    OrderService orderService;

    private static final Logger LOG = LoggerFactory.getLogger(PaymentService.class);

    public void processPayment(Payment payment) {
        if (!orderService.isOrderValid(payment.getOrderId())) {
            LOG.warn("Cannot process payment. Order ID {} does not exist.", payment.getOrderId());
            return;
        }

        System.out.println("Payment processed locally: " + payment);
        System.out.println("Sending payment to Kafka: " + payment);
        paymentProducer.sendPayment(payment.getOrderId(), payment);
        System.out.println("Payment info sent to Kafka successfully.");
    }
}
