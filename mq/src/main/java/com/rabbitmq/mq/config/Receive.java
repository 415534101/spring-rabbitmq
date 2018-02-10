package com.rabbitmq.mq.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Payload;

@Configuration
@RabbitListener(queues = "test_spring_queue")
public class Receive {

	private static final Logger logger = LoggerFactory.getLogger(Receive.class);

	@RabbitHandler
	public void process(@Payload String queue) {
		logger.info("[receive] listener - {}", queue);
	}

}
