package com.acme.healthcare.audit.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Declares messaging infrastructure for audit event ingestion.
 */
@Configuration
public class AuditMessagingConfiguration {

    @Value("${messaging.audit.exchange:audit.events}")
    private String auditExchange;

    @Value("${messaging.audit.queue:audit-service.events}")
    private String auditQueue;

    /**
     * Fanout exchange broadcasting audit events.
     *
     * @return exchange
     */
    @Bean
    public FanoutExchange auditFanoutExchange() {
        return new FanoutExchange(auditExchange, true, false);
    }

    /**
     * Queue consumed by the audit service.
     *
     * @return queue
     */
    @Bean
    public Queue auditQueue() {
        return new Queue(auditQueue, true);
    }

    /**
     * Binds the queue to the exchange.
     *
     * @param queue queue
     * @param exchange exchange
     * @return binding
     */
    @Bean
    public Binding auditBinding(final Queue queue, final FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    /**
     * Configures a JSON converter for inbound messages.
     *
     * @return message converter
     */
    @Bean
    public MessageConverter auditMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

