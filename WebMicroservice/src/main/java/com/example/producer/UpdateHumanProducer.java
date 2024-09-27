package com.example.producer;

import com.example.dto.UpdateHumanRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UpdateHumanProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateHumanProducer.class);

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.update.human.routing.key.name}")
    private String routingKey;

    private RabbitTemplate rabbitTemplate;

    public UpdateHumanProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendUpdateMessage(UpdateHumanRequest dto) {
        LOGGER.info(String.format("Sending update message: %s", dto.toString()));
        rabbitTemplate.convertAndSend(exchange, routingKey, dto);
    }
}
