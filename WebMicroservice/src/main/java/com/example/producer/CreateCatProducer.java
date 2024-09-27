package com.example.producer;

import com.example.dto.CreateCatRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CreateCatProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateCatProducer.class);

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.create.cat.routing.key.name}")
    private String routingKey;

    private RabbitTemplate rabbitTemplate;

    public CreateCatProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCreateMessage(CreateCatRequest dto) {
        LOGGER.info(String.format("Sending create message: %s", dto.toString()));
        rabbitTemplate.convertAndSend(exchange, routingKey, dto);
    }
}
