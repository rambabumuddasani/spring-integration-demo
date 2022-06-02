package com.example.app.controller;

import com.example.app.gateway.CustomGateway;
import lombok.AllArgsConstructor;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class GatewayController {
    private final CustomGateway customGateway;

    @GetMapping("/invokeWithDirectChannel")
    public String invoke(){
        Message<String> msg = MessageBuilder.withPayload("india is a beautiful country").build();
        System.out.println(getClass().getName()+" Thread.currentThread "+Thread.currentThread());
         customGateway.print(msg);
         return "Happy";
    }

    @GetMapping("/invokeWithExecutorChannel")
    public String invoke2(){
        Message<String> msg = MessageBuilder.withPayload("india is a beautiful country").build();
        System.out.println(getClass().getName()+" Thread.currentThread "+Thread.currentThread());
        customGateway.print2(msg);
        return "Happy";
    }

}
