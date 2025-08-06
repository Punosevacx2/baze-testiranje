package com.example.demo.redis;


import org.springframework.stereotype.Component;

import com.example.demo.websocket.WebSocketNotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Component
public class RedisSubscriber implements MessageListener {

	 @Autowired
	    private WebSocketNotificationService websocketService;

	    @Override
	    public void onMessage(Message message, byte[] pattern) {
	        String msg = message.toString();
	        websocketService.sendToAllClients(msg);
	    }
	}