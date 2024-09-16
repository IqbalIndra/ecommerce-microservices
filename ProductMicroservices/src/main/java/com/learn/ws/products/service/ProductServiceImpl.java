package com.learn.ws.products.service;

import com.learn.ws.products.rest.CreateProductRestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductServiceImpl implements ProductService{
    KafkaTemplate<String,ProductCreatedEvent> kafkaTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createProduct(CreateProductRestModel product) {
        String productId = UUID.randomUUID().toString();

        var productCreatedEvent = new ProductCreatedEvent(productId,product.title(),
                product.price(),product.quantity());

        CompletableFuture<SendResult<String, ProductCreatedEvent>> future = kafkaTemplate.send("product-created-event-topic", productId, productCreatedEvent);

        future.whenComplete((result, exception) -> {
           if(exception !=null)
               LOGGER.error("failed to send message -> {}" , exception.getMessage());
           else
               LOGGER.info("Message send successfully -> {}", result.getRecordMetadata());
        });

        return productId;
    }
}
