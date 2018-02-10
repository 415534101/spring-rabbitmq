package com.rabbitmq.mq;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class MqApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(MqApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("success");
		
	}
}
