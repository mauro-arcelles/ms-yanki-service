# Yanki Microservice

Spring Boot Webflux microservice that handles wallet operations.

## Stack
- Java 11
- Spring Boot 2.x
- Spring Webflux
- Spring Cloud Config Client
- Reactive Mongodb
- Openapi contract first
- Swagger ui
- Spring Kafka

## Configuration
Service connects to Config Server using:
```properties
spring.application.name=ms-yanki-service
spring.config.import=optional:configserver:http://localhost:8888
```
for properties
```yaml
eureka:
  instance:
    hostname: localhost
    instance-id: ${spring.application.name}:${random.int}
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
      
spring:
  data:
    mongodb:
      host: localhost
      port: 27017
      database: ms-bootcamp-arcelles

server:
  port: ${PORT:0}

application:
  config:
    kafka:
      topic-name: wallet-transactions
      bootstrap-servers: localhost:9092

springdoc:
  api-docs:
    path: /yanki-docs/v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
  webjars:
    prefix:
```

## Swagger
http://localhost:9090/swagger-ui.html

![ms-yanki-service-2025-03-14-160844](https://github.com/user-attachments/assets/effe4c87-98b1-4ecc-a9ef-1b3e619bc6df)
