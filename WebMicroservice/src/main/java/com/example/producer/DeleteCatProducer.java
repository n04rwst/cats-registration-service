package com.example.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DeleteCatProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteCatProducer.class);

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.delete.cat.routing.key.name}")
    private String routingKey;

    private RabbitTemplate rabbitTemplate;

    public DeleteCatProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCreateMessage(Integer id) {
        LOGGER.info(String.format("Sending create message: %d", id));
        rabbitTemplate.convertAndSend(exchange, routingKey, id);
    }
}
