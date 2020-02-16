package com.example.controller;

import com.example.domain.board.AlertMessage;
import com.example.domain.board.BoardEntity;
import com.example.domain.board.CommentEntity;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class WebSocketController {

  @MessageMapping("/anb")
  @SendTo("/topic/message")
  public AlertMessage alertNewBoard(BoardEntity board, CommentEntity comment) throws Exception {
    log.info(board.toString());
    log.info(comment.toString());
    AlertMessage alertMessage = new AlertMessage("good");
    Thread.sleep(1000); // simulated delay
    return alertMessage;
  }

}