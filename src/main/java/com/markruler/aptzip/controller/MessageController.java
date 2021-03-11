package com.markruler.aptzip.controller;

import com.markruler.aptzip.domain.user.AlertMessage;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@lombok.extern.slf4j.Slf4j
@RestController
public class MessageController {

  /**
   * 클라이언트는 @MessageMapping 으로 request 서버는 @SendTo 로 response
   */
  @MessageMapping("/nbax") // 전역 RequestMapping: root path
  @SendTo("/topic/messagexx") // publishing
  public AlertMessage newBoardAlertx(@RequestBody AlertMessage alert) throws Exception {
    log.debug("STOMP >> " + alert.getMsg());
    Thread.sleep(1000); // simulated delay
    return alert;
  }

  @Deprecated
  @MessageMapping("/nbaxx")
  @SendTo("/topic/messagexx") // publishing
  public String newBoardAlertxx(String message) throws Exception {
    log.debug("STOMP >> " + message);
    return message;
  }

  @GetMapping("/api/message/users/{id}")
  public String message() {
    return "user/page-messages";
  }
}
