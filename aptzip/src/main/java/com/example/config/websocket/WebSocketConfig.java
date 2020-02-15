package com.example.config.websocket;

import com.example.config.websocket.stateful.CommentEchoHandler;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    // pure websocket (standard) > IE 10
    registry.addHandler(new CommentEchoHandler(), "/ws/comment").setAllowedOrigins("*");

    // Use SockJS Library > IE 8
    // registry.addHandler(new CommentEchoHandler(), "/ws/comment").setAllowedOrigins("*").withSockJS();
  }
  
}