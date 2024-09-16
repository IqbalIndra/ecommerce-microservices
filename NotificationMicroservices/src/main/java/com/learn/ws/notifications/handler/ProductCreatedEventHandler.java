package com.learn.ws.notifications.handler;

import com.learn.ws.notifications.error.NotRetryableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.learn.ws.core.ProductCreatedEvent;

@Component
@KafkaListener(topics = {"product-created-event-topic"})
public class ProductCreatedEventHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @KafkaHandler
    public void handler(ProductCreatedEvent productCreatedEvent){
        if(true) throw new NotRetryableException("This is data must be send to Dead Letter Topic DLT");
        LOGGER.info("Received a new Event : {}" , productCreatedEvent);
    }
}
