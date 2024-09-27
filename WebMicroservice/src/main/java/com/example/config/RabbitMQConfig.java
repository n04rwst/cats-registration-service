package com.example.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.create.human.queue.name}")
    private String createHumanQueueName;

    @Value("${rabbitmq.create.human.routing.key.name}")
    private String createHumanRoutingKey;

    @Value("${rabbitmq.update.human.queue.name}")
    private String updateHumanQueueName;

    @Value("${rabbitmq.update.human.routing.key.name}")
    private String updateHumanRoutingKey;

    @Value("${rabbitmq.delete.human.queue.name}")
    private String deleteHumanQueueName;

    @Value("${rabbitmq.delete.human.routing.key.name}")
    private String deleteHumanRoutingKey;

    @Value("${rabbitmq.create.cat.queue.name}")
    private String createCatQueueName;

    @Value("${rabbitmq.create.cat.routing.key.name}")
    private String createCatRoutingKey;

    @Value("${rabbitmq.update.cat.queue.name}")
    private String updateCatQueueName;

    @Value("${rabbitmq.update.cat.routing.key.name}")
    private String updateCatRoutingKey;

    @Value("${rabbitmq.delete.cat.queue.name}")
    private String deleteCatQueueName;

    @Value("${rabbitmq.delete.cat.routing.key.name}")
    private String deleteCatRoutingKey;

    @Value("${rabbitmq.save.cat.info.queue.name}")
    private String saveInfoCatQueueName;

    @Value("${rabbitmq.delete.cat.info.queue.name}")
    private String deleteInfoCatQueueName;

    @Value("${rabbitmq.save.cat.info}")
    private String saveInfoCatRoutingKeyName;

    @Value("${rabbitmq.delete.cat.info}")
    private String deleteInfoCatRoutingKeyName;

    @Bean
    public TopicExchange getExchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Queue createHumanQueue() {
        return new Queue(createHumanQueueName);
    }

    @Bean
    public Queue updateHumanQueue() {
        return new Queue(updateHumanQueueName);
    }

    @Bean
    public Queue deleteHumanQueue() {
        return new Queue(deleteHumanQueueName);
    }

    @Bean
    public Queue createCatQueue() {
        return new Queue(createCatQueueName);
    }

    @Bean
    public Queue updateCatQueue() {
        return new Queue(updateCatQueueName);
    }

    @Bean
    public Queue deleteCatQueue() {
        return new Queue(deleteCatQueueName);
    }

    @Bean
    public Queue saveInfoCatQueue() {
        return new Queue(saveInfoCatQueueName);
    }

    @Bean
    public Queue deleteInfoCatQueue() {
        return new Queue(deleteInfoCatQueueName);
    }

    @Bean
    public Binding createHumanBinding() {
        return BindingBuilder
                .bind(createHumanQueue())
                .to(getExchange())
                .with(createHumanRoutingKey);
    }

    @Bean
    public Binding updateHumanBinding() {
        return BindingBuilder
                .bind(updateHumanQueue())
                .to(getExchange())
                .with(updateHumanRoutingKey);
    }

    @Bean
    public Binding deleteHumanBinding() {
        return BindingBuilder
                .bind(deleteHumanQueue())
                .to(getExchange())
                .with(deleteHumanRoutingKey);
    }

    @Bean
    public Binding createCatBinding() {
        return BindingBuilder
                .bind(createCatQueue())
                .to(getExchange())
                .with(createCatRoutingKey);
    }

    @Bean
    public Binding updateCatBinding() {
        return BindingBuilder
                .bind(updateCatQueue())
                .to(getExchange())
                .with(updateCatRoutingKey);
    }

    @Bean
    public Binding deleteCatBinding() {
        return BindingBuilder
                .bind(deleteCatQueue())
                .to(getExchange())
                .with(deleteCatRoutingKey);
    }

    @Bean
    public Binding saveInfoCatBinding() {
        return BindingBuilder
                .bind(saveInfoCatQueue())
                .to(getExchange())
                .with(saveInfoCatRoutingKeyName);
    }

    @Bean
    public Binding deleteInfoCatBinding() {
        return BindingBuilder
                .bind(deleteInfoCatQueue())
                .to(getExchange())
                .with(deleteInfoCatRoutingKeyName);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
