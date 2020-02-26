package com.example.controller;

import com.example.domain.user.MessageEntity;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class MessageController {
  
  @MessageMapping("/msg")
  @SendTo("/topic/message")
  public MessageEntity alertNewBoard(@RequestBody MessageEntity msg) throws Exception {
    log.info(msg.toString());
    
    MessageEntity alertMessage = new MessageEntity("good", msg.getUser());
    Thread.sleep(1000); // simulated delay
    return alertMessage;
  }
  
  /**
	 * message
	 * @return
	 */
	@GetMapping("/{id}/message")
	public String message() {
		return "user/page-messages";
	}
}