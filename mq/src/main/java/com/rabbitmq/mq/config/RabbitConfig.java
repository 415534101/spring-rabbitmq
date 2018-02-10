package com.rabbitmq.mq.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class RabbitConfig {
	private static final Logger logger = LoggerFactory.getLogger(RabbitConfig.class);
	@Value("${rabbitmq.server}")
	private String host;
	@Value("${rabbitmq.port}")
	private Integer port;
	@Value("${rabbitmq.virtual-host}")
	private String vhost;
	@Value("${rabbitmq.username}")
	private String username;
	@Value("${rabbitmq.password}")
	private String password;
	@Value("${rabbitmq.pull-queue}")
	private String pullQueue;
	@Value("${rabbitmq.pull-exchange}")
	private String pullExchange;
	@Value("${rabbitmq.routing-key}")
	private String routingKey;
	
	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory factory = new CachingConnectionFactory();
		factory.setHost(this.host);
		factory.setPort(this.port);
		factory.setVirtualHost(this.vhost);
		factory.setUsername(this.username);
		factory.setPassword(this.password);
		factory.setPublisherConfirms(true);
		
//		Channel channel = factory.connect
//		
//		try {
//			channel.queueDeclare(this.pullQueue, false, false, false, null);
//			channel.exchangeDeclare(this.pullExchange, "direct");
//			channel.queueBind(this.pullQueue, this.pullExchange, this.routingKey);
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}finally {
//			try {
//				channel.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (TimeoutException e) {
//				e.printStackTrace();
//			}
//		}
		return factory;
	}
	@Bean  
	public SimpleMessageListenerContainer messageContainer() {  
	    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());  
	    container.setQueues(defaultQueue());  
	    container.setExposeListenerChannel(true);  
	    container.setMaxConcurrentConsumers(1);  
	    container.setConcurrentConsumers(1);  
	    container.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认  
	    container.setMessageListener((ChannelAwareMessageListener) (message, channel) ->{
	            byte[] body = message.getBody();
	            logger.info("Listener onMessage : " + new String(body, "utf-8"));
	            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //确认消息成功消费

	    });
	return container;
	}  
	
	
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMessageConverter(new Jackson2JsonMessageConverter());
		return template;
	}
	
	@Bean(name="queue")
	public String getQueue() {
		return this.pullQueue;
	}
	@Bean(name="exchange")
	public String getExchange() {
		return this.pullExchange;
	}
	@Bean(name="routingKey")
	public String getRoutingKey() {
		return this.routingKey;
	}
	@Bean
	public DirectExchange defaultExchange() {
		return new DirectExchange(this.pullExchange);
	}
	
	@Bean
	public Queue defaultQueue() {
		return new Queue(this.pullQueue);
	}
	
	 
	@Bean
	public Binding binding() {
		return BindingBuilder.bind(defaultQueue()).to(defaultExchange()).with(routingKey);
	}
	
}
