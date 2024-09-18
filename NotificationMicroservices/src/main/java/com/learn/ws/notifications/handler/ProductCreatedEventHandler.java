package com.learn.ws.notifications.handler;

import com.learn.ws.notifications.error.NotRetryableException;
import com.learn.ws.notifications.error.RetryableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.learn.ws.core.ProductCreatedEvent;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
@KafkaListener(topics = {"product-created-event-topic"})
public class ProductCreatedEventHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final RestTemplate restTemplate;

    public ProductCreatedEventHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @KafkaHandler
    public void handler(ProductCreatedEvent productCreatedEvent){
        LOGGER.info("Received a new Event : {}" , productCreatedEvent);

        String url = "http://localhost:8082/response/200";

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            if(response.getStatusCode().value() == HttpStatus.OK.value()){
                LOGGER.info("Successfully send data to another service via Http => {}",response.getBody());
            }
        }catch (ResourceAccessException ex){
            LOGGER.error("Resource Error When send data to Another Service ");
            throw new RetryableException(ex.getMessage());
        }catch (HttpServerErrorException ex){
            LOGGER.error("Http Server Error When send data to Another Service");
            throw new NotRetryableException(ex.getMessage());
        }catch (Exception ex){
            LOGGER.error("Error Exception");
            throw new NotRetryableException(ex.getMessage());
        }
    }
}
