package com.lucasnunesg.banksystem.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String NOTIFICATION_QUEUE = "notification.queue";
    public static final String EXCHANGE = "notification.exchange";
    public static final String ROUTING_KEY = "notification.#";

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE, true);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange topicExchange) {
        return new Binding(
                queue.getName(),
                Binding.DestinationType.QUEUE,
                topicExchange.getName(),
                ROUTING_KEY,
                null);
    }
}
