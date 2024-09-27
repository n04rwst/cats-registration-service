package com.example.producer;

import com.example.dto.CreateHumanRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DeleteHumanProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteHumanProducer.class);

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.delete.human.routing.key.name}")
    private String routingKey;

    private RabbitTemplate rabbitTemplate;

    public DeleteHumanProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendDeleteMessage(Integer id) {
        LOGGER.info(String.format("Sending delete message: %d", id));
        rabbitTemplate.convertAndSend(exchange, routingKey, id);
    }
}
