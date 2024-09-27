package com.example.producer;

import com.example.dto.CreateHumanRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CreateHumanProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateHumanProducer.class);

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.create.human.routing.key.name}")
    private String routingKey;

    private RabbitTemplate rabbitTemplate;

    public CreateHumanProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCreateMessage(CreateHumanRequest dto) {
        LOGGER.info(String.format("Sending create message: %s", dto.toString()));
        rabbitTemplate.convertAndSend(exchange, routingKey, dto);
    }
}
