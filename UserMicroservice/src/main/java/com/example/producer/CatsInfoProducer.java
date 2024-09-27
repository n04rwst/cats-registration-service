package com.example.producer;

import com.example.entity.Cat;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CatsInfoProducer {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.save.cat.info}")
    private String saveRoutingKeyName;

    @Value("${rabbitmq.delete.cat.info}")
    private String deleteRoutingKeyName;

    private RabbitTemplate rabbitTemplate;

    public CatsInfoProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCatsInfoToSave(Set<Cat> cats) {
        rabbitTemplate.convertAndSend(exchangeName, saveRoutingKeyName, cats);
    }

    public void sendCatsInfoToDelete(Set<Cat> cats) {
        rabbitTemplate.convertAndSend(exchangeName, deleteRoutingKeyName, cats);
    }
}
