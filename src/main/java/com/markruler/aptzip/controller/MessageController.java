package com.markruler.aptzip.controller;

import com.markruler.aptzip.domain.user.model.AlertMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api(tags = "message")
@RestController
public class MessageController {
  Logger log = LoggerFactory.getLogger(MessageController.class);

  /**
   * 클라이언트는 @MessageMapping 으로 request 서버는 @SendTo 로 response
   */
  @MessageMapping("/nbax") // 전역 RequestMapping: root path
  @SendTo("/topic/messagexx") // publishing
  public AlertMessage newBoardAlertx(@RequestBody AlertMessage alert) throws Exception {
    log.debug("STOMP >> {}", alert.getMsg());
    Thread.sleep(1000); // simulated delay
    return alert;
  }

  @Deprecated
  @MessageMapping("/nbaxx")
  @SendTo("/topic/messagexx") // publishing
  public String newBoardAlertxx(String message) throws Exception {
    log.debug("STOMP >> {}", message);
    return message;
  }

  @GetMapping("/api/message/users/{id}")
  public String message() {
    return "user/page-messages";
  }
}
