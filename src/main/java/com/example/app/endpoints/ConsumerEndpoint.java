package com.example.app.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class ConsumerEndpoint {

	@Autowired
	MessageChannel outputMessageChannel;

	@ServiceActivator(inputChannel = "myOutputChannel")
	public void consumeStringMessage(String message) {
		System.out.println("Received message from myOutputChannel : " + message+" Thread.currentThread "+Thread.currentThread());
	}

	@ServiceActivator(inputChannel = "myInputChannel", outputChannel = "myOutputChannel")
	public String toUppercase(Message<String> message) {
		System.out.println("Received message from myInputChannel : " + message.getPayload()+" Thread.currentThread "+Thread.currentThread());
		return message.getPayload().toUpperCase();
	}

	@ServiceActivator(inputChannel = "inputMessageChannel", outputChannel = "outputMessageChannel")
	public String toUppercase2(Message<String> message) {
		System.out.println(" toUppercase2 Received message from myInputChannel : " + message.getPayload()+" Thread.currentThread "+Thread.currentThread());
		return message.getPayload().toUpperCase();
	}

	@ServiceActivator(inputChannel = "inputMessageChannel", outputChannel = "routingChannel")
	public Message<String> toUppercase3(Message<String> message) {
		Message<String> msg = MessageBuilder.withPayload(message.getPayload())
				.setHeader("VALID_SIGNATURE",true)
				.build();
		System.out.println(" toUppercase3 Received message from myInputChannel : " + msg.getPayload()+" headers "
				+msg.getHeaders()+ " Thread.currentThread"+Thread.currentThread());
		return msg;
	}


	@Router(inputChannel = "routingChannel")
	public MessageChannel routeBasedOnHeader(@Header("VALID_SIGNATURE") boolean validSignature) {
		System.out.println("routeBasedOnHeader  Thread.currentThread "+Thread.currentThread());
		return outputMessageChannel;
	}

	@ServiceActivator(inputChannel = "outputMessageChannel")
	public void consumeStringMessage2(String message) {
		System.out.println(" consumeStringMessage2 Received message from myOutputChannel : " + message+" Thread.currentThread "+Thread.currentThread());
	}

}


