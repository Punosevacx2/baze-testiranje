package com.example.demo.redis;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.DTO.MessageDTO;
import com.example.demo.mongo.entities.ProjectMessage;
import com.example.demo.mongo.repository.ChatMessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/chat")
public class RedisChatController {

	
	  private final RedisMessagePublisher redisMessagePublisher;
	  
	  private final ChatMessageRepository chatMessageRepository;
	  
	  @Autowired
	  private SimpMessagingTemplate messagingTemplate;

	  
	  public RedisChatController(RedisMessagePublisher redisMessagePublisher,ChatMessageRepository chatMessageRepository) {
		  this.redisMessagePublisher=redisMessagePublisher;
		  this.chatMessageRepository=chatMessageRepository;
		  
	  }

	  
	  @MessageMapping("/chat.sendMessage")
	  public void sendMessage(@Payload MessageDTO chatMessage) {
		  ProjectMessage pm = new ProjectMessage();
		  pm.setContent(chatMessage.getContent());
		  pm.setProjectId(chatMessage.getProjectId());
		  pm.setSenderName(chatMessage.getSenderName());
		  pm.setSenderId(chatMessage.getSenderId());
		  pm.setTimestamp(LocalDateTime.now());
		  chatMessageRepository.save(pm);

		  String destination = "/topic/project/" + chatMessage.getProjectId();
		  messagingTemplate.convertAndSend(destination, pm);
	  }
	  
	    
	}


