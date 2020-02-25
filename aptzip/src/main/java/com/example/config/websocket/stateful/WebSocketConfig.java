package com.example.config.websocket.stateful;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    /**
     * Use WebSocket (You don't need a library)
     * 도메인이 다른 서버에서도 접속 가능하도록 CORS
     * setAllowedOrigins("*")
     */

    // pure websocket (standard) > IE 10
    registry.addHandler(new CommentEchoHandler(), "/ws/comment")
            .setAllowedOrigins("*");
            // .setHandshakeHandler();

    // Use SockJS Library > IE 8
    // client js 라이브러리 필요(cdn || webjar)
    // endpoint
    // registry.addHandler(new CommentEchoHandler(), "/ws/message")
    //         .setAllowedOrigins("*")
    //         .withSockJS();
  }
  
}