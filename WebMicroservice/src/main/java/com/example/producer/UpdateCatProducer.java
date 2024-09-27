package com.example.producer;

import com.example.dto.UpdateCatRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UpdateCatProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateCatProducer.class);

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.update.cat.routing.key.name}")
    private String routingKey;

    private RabbitTemplate rabbitTemplate;

    public UpdateCatProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendUpdateMessage(UpdateCatRequest dto) {
        LOGGER.info(String.format("Sending update message: %s", dto.toString()));
        rabbitTemplate.convertAndSend(exchange, routingKey, dto);
    }
}
