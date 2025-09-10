package com.example.demo.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

	//@Configuration
	//@EnableWebSocketMessageBroker
	public class WebSocketNotificationService implements WebSocketMessageBrokerConfigurer {

	    @Override
	    public void registerStompEndpoints(StompEndpointRegistry registry) {
	        // Endpoint na koji frontend može da se konektuje
	        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
	    }

	    @Override
	    public void configureMessageBroker(MessageBrokerRegistry registry) {
	        registry.setApplicationDestinationPrefixes("/chat");  // frontend publish na /chat/chat.sendMessage
	        registry.enableSimpleBroker("/topic");   
	        //registry.setApplicationDestinationPrefixes("/app");// frontend subscribe na /topic/messages
	    }
	}