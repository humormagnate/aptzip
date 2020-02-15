package com.example.config.websocket.stateful;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.domain.user.UserResponseDto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommentEchoHandler extends TextWebSocketHandler {
  
  /*
    STOMP 가 아니라 WebSocket 사용 시
  */
  private final List<WebSocketSession> sessionList = new ArrayList<>();
  private final Map<String, WebSocketSession> userSessions = new HashMap<>();

  // Invoked after WebSocket negotiation has succeeded
  // and the WebSocket connection is opened and ready for use.
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    log.info("afterConnectionEstablished >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + session);
    sessionList.add(session);
    String senderId = getId(session);
    userSessions.put(senderId, session);
	}

  // Invoked when a new WebSocket message arrives.
  @Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    log.info("handleTextMessage >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + session + " : " + message);
    String senderId = getId(session);

    // for (WebSocketSession webSocketSession : sessionList) {
    //   webSocketSession.sendMessage(new TextMessage(senderId + " : " + message.getPayload()));
    // }
    
    // protocol : 댓글, 작성자, 게시글작성자, 게시글번호 (comment, from, to, id)
    String msg = message.getPayload();
    if (StringUtils.isNotEmpty(msg)) {
      String[] strs = msg.split(",");
      if (strs != null && strs.length == 4) {
        String comment = strs[0];
        String commentWriter = strs[1];
        String boardWriter = strs[2];
        String boardId = strs[3];

        WebSocketSession boardSocketSession = userSessions.get(boardWriter);
        if ("comment".equals(comment) && boardSocketSession != null) {
          TextMessage tm = new TextMessage(commentWriter + "님이 " + boardId + "번 게시글에 댓글을 달았습니다!");
          boardSocketSession.sendMessage(tm);
        }
      }
    }
  }
  
  private String getId(WebSocketSession session) {
    Map<String, Object> httpSession = session.getAttributes();
    UserResponseDto principal = (UserResponseDto)httpSession.get("principal");
    if (principal == null) {
      return session.getId();
    } else {
      return String.valueOf(principal.getId());
    }
  }

  // Invoked after the WebSocket connection has been closed by either side,
  // or after a transport error has occurred.
  //
  // Although the session may technically still be open, depending on the underlying implementation,
  // sending messages at this point is discouraged and most likely will not succeed.
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    log.info("afterConnectionClosed >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + session + " : " + status);
    sessionList.remove(session);
	}
}