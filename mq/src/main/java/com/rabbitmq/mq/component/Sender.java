package com.rabbitmq.mq.component;

import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sender implements ConfirmCallback {

	private static final Logger logger = LoggerFactory.getLogger(Sender.class);
	private RabbitTemplate template;
	
	@Resource(name="queue")
	private String pullQueue;
	@Resource(name="exchange")
	private String pullExchange;
	@Resource(name="routingKey")
	private String routingKey;
	@Autowired
	public Sender (RabbitTemplate template) {
		this.template = template;
		template.setConfirmCallback(this);
	}
	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		logger.info("[confirm] - msg id - {}", correlationData.getId());
		logger.info("[confirm] - ack - {}", ack );
		logger.info("[confirm] - cause - {}", cause);
	}
	
	public void send(String msg) {
		CorrelationData data = new CorrelationData(UUID.randomUUID().toString());
		logger.info("[send] uuid - {}", data.getId());

		this.template.convertAndSend(this.pullExchange, routingKey, "hello spring rabbitMQ", data);
	}

}
