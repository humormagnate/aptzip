package com.example.config.websocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class ReplyEchoHandler extends TextWebSocketHandler {
  
  // Invoked after WebSocket negotiation has succeeded
  // and the WebSocket connection is opened and ready for use.
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {

	}

  // Invoked when a new WebSocket message arrives.
  @Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    
	}

  // Invoked after the WebSocket connection has been closed by either side,
  // or after a transport error has occurred.
  // Although the session may technically still be open, depending on the underlying implementation,
  // sending messages at this point is discouraged and most likely will not succeed.
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

	}
}