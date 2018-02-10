package com.rabbitmq.mq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rabbitmq.mq.component.Sender;

@RestController
public class TestController {
	@Autowired
	private Sender sender;

	@ResponseBody
	@RequestMapping(value = "/send")
	public String send(String msg) {
		sender.send(msg);
		return "Send OK";
	}
}
