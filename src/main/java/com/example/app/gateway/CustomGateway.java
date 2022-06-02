package com.example.app.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

@MessagingGateway
public interface CustomGateway {

	@Gateway(requestChannel = "myInputChannel")
	public void print(Message<String> message);

	@Gateway(requestChannel = "inputMessageChannel")
	public void print2(Message<String> message);

}
