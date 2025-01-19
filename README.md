# NewKart

The **NewKart** is a Kafka-enabled microservice built using **Java 17** and **Micronaut 4.7.1**. It facilitates the creation, processing, and validation of orders and payments within an e-commerce system. With a robust architecture, the project integrates Kafka for asynchronous communication and provides reliable failure handling mechanisms such as Dead Letter Queues (DLQ).

---


### **RESTful API**
- Endpoints for order and payment creation, validation, and status checks.
<img width="1792" alt="Screenshot 2024-12-05 at 2 32 58 PM" src="https://github.com/user-attachments/assets/93f85030-f5f0-4265-8620-70df19223566" />

### **Terminal Output**
<img width="993" alt="Screenshot 2024-12-11 at 8 24 14 PM" src="https://github.com/user-attachments/assets/728ee2ed-137a-4c09-aa8a-a256ee06caea" />

---

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/harshit-sharma1256/NewKart.git
```

---

### 2. Set Up Kafka and PostgreSQL with Docker

1. **Start Colima**:
   ```bash
    Colima start
    ```

2. **Run Kafka, Zookeeper, and PostgreSQL**:
   Use the provided `docker-compose.yml` to spin up Kafka, Zookeeper, and PostgreSQL:
   ```bash
   docker-compose up -d
   ```
   Ensure the configuration file is correctly set up.

---

### 3. Build the Project

```bash
./gradlew build
```

---

### 4. Run the Application

```bash
./gradlew run
```

---

### 5. Access the Application

- **Swagger UI**: Access the API documentation at `http://localhost:8080/swagger-ui#/Order%20Processing/createOrder`.

---

## API Endpoints

| Method | Endpoint              | Description                          |
|--------|-----------------------|--------------------------------------|
| GET    | `/orders/health`      | Checks the health of the orders API. |
| POST   | `/orders/create`      | Creates a new order and sends to Kafka. |
| GET    | `/payments/health`    | Checks the health of the payments API. |
| POST   | `/payments/process`   | Processes a new payment and sends to Kafka. |

---

## Running Tests

Run the unit tests using:

```bash
./gradlew test
```

---
### **Steps to Run Kafka Commands in a Dockerized Kafka Cluster**

1. **Find the Kafka Container Name**  
   Run the following command to list all running containers and identify your Kafka container name:
   ```bash
   docker ps
   ```
   Look for the container running the Kafka image (e.g., `confluentinc/cp-kafka`) and note its container name or ID.

2. **Access the Kafka Container**  
   Use the container name or ID to open a shell inside the Kafka container:
   ```bash
   docker exec -it <kafka-container-name> bash
   ```

3. **Run the Kafka Topics Command**  
   Inside the container, you can use the Kafka CLI tools. To alter the topic `order_topic` and set the number of partitions to 2, run:
   ```bash
   kafka-topics --bootstrap-server kafka:29092 --alter --topic order_topic --partitions 2
   ```

   - Replace `kafka:29092` with the advertised listener of your Kafka broker inside the container (as defined in your `docker-compose.yml`).
   - Make sure the topic already exists before attempting to alter it.

4. **Verify the Changes**  
   Check the topic details to confirm the partition count:
   ```bash
   kafka-topics --bootstrap-server kafka:29092 --describe --topic order_topic
   ```

---

### **Common Issues**
1. **Connection Issues**: Ensure the `bootstrap-server` matches the Kafka configuration inside the container (e.g., `kafka:29092`).
2. **Partition Reduction**: Kafka does not support reducing the number of partitions for a topic. You can only increase the partition count.

---

### **Automating in Docker-Compose**
If you want to set up partitions automatically during container startup, you can add a `command` in the Kafka service definition in `docker-compose.yml`:
```yaml
services:
  kafka:
    ...
    command: >
      sh -c "kafka-topics --create --topic order_topic --partitions 2 --replication-factor 1 --if-not-exists --bootstrap-server kafka:9092 &&
             kafka-server-start /etc/kafka/server.properties"
```

This ensures the topic is created with the desired number of partitions at container startup.

## Features

### **Order Management**
- Create and validate orders.
- Prevent duplicate orders through deduplication.
- Publish and consume order events using Kafka.

### **Payment Management**
- Process payments for orders.
- Publish and consume payment events.
- Retry mechanism for payment failures with DLQ handling.

### **Kafka Integration**
- **OrderProducer**: Publishes order-related messages to Kafka topics.
- **OrderConsumer**: Consumes and processes order-related messages.
- **PaymentProducer**: Publishes payment-related messages to Kafka topics.
- **PaymentConsumer**: Consumes and processes payment-related messages.
- **DLQ**: Handles failures during order and payment processing.
-
## Technology Stack

- **Java 17**
- **Micronaut 3.10.1**
- **Kafka**: Message broker for communication.
- **JUnit 5**: Unit testing framework.
- **Mockito**: Mocking framework for testing.
- **Gradle**: Build tool.

---

## Prerequisites

- **Java 17** installed.
- **Docker(Colima)** for running Kafka, Zookeeper, and PostgreSQL.
- **Micronaut CLI** for easier project management (optional).

---

## Project Structure

The project is organized as follows:

```
src
├── main
│   ├── java
│   │   └── com
│   │       └── harshit
│   │           ├── Application.java
│   │           ├── consumers
│   │           │   ├── OrderConsumer.java
│   │           │   └── PaymentConsumer.java
│   │           ├── controller
│   │           │   ├── OrderController.java
│   │           │   └── PaymentController.java
│   │           ├── entity
│   │           │   ├── Order.java
│   │           │   └── Payment.java
│   │           ├── producers
│   │           │   ├── DlqProducer.java
│   │           │   ├── OrderProducer.java
│   │           │   └── PaymentProducer.java
│   │           └── service
│   │               ├── OrderService.java
│   │               └── PaymentService.java
│   └── resources
│       ├── application.properties
│       └── logback.xml
└── test
    └── java
        └── com
            └── harshit
                ├── OrderControllerTest.java
                ├── PaymentControllerTest.java
                ├── OrderServiceTest.java
                ├── PaymentServiceTest.java
                ├── OrderConsumerTest.java
                └── PaymentConsumerTest.java
```

---

## Future Enhancements

- **Comprehensive Reporting**: Generate detailed reports for orders and payments.
- **Advanced Security**: Add authentication and authorization for API endpoints.

---

## Contributing

Contributions are welcome! Please fork the repository, make your changes, and submit a pull request.

---


## Acknowledgments

This project leverages the power of Micronaut and Kafka to demonstrate a scalable e-commerce system.

To run the `kafka-topics` command inside a Docker container using Colima, you need to execute the command within the Kafka container itself. Here’s how you can do it:

---


## Micronaut 4.7.1 Documentation

- [User Guide](https://docs.micronaut.io/4.7.1/guide/index.html)
- [API Reference](https://docs.micronaut.io/4.7.1/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/4.7.1/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)



