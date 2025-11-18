package com.acme.healthcare.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * Declares messaging infrastructure used for publishing audit events.
 */
@Configuration
@ConditionalOnBean(RabbitTemplate.class)
public class AuditMessagingConfiguration {

    @Value("${messaging.audit.exchange:audit.events}")
    private String auditExchange;

    /**
     * Declares the fanout exchange used to broadcast audit events.
     *
     * @return fanout exchange
     */
    @Bean
    public FanoutExchange auditFanoutExchange() {
        return new FanoutExchange(auditExchange, true, false);
    }

    /**
     * Configures a JSON message converter for audit events.
     *
     * @return message converter
     */
    @Bean
    public MessageConverter auditMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

