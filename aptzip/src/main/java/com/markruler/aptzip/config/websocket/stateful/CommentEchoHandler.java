package com.markruler.aptzip.config.websocket.stateful;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * Basic(Pure) WebSocket
 */
@Slf4j
public class CommentEchoHandler extends TextWebSocketHandler {

  // broadcast
  private final List<WebSocketSession> sessionList = new ArrayList<>();
  // unicast
  private final Map<String, WebSocketSession> userSessions = new HashMap<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    log.info("afterConnectionEstablished >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> {}", session);
    sessionList.add(session);
    String senderId = getId(session);
    userSessions.put(senderId, session);
	}

  @Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    log.info("handleTextMessage >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>{}:{}", session, message);
    
    // broadcast
    // String senderId = getId(session);
    // for (WebSocketSession webSocketSession : sessionList) {
    //   webSocketSession.sendMessage(new TextMessage(senderId + " : " + message.getPayload()));
    //   log.info("broadcasting test success");
    // }
    
    // unicast
    // 댓글, 작성자, 게시글작성자, 게시글번호 (comment, comment_user_id, board_user_id, board_id)
    String payload = message.getPayload();
    log.info("payload {}", payload);

    if (StringUtils.isNotEmpty(payload)) {
      // "|"(pipe)로만 하면 1글자씩 전부 나눠버림
      // "+"(plus)가 포함되어 있으면 dangling meta character '+' near index 에러
      log.info("split");
      String[] strs = payload.split("\\|\\+\\|");
      log.info("comment {} / commentUser {} / boardUser {}", strs[0], strs[1], strs[2]);
      log.info("strs.length : {}", strs.length);
      log.info("session.getPrincipal() : {}", session.getPrincipal());

      if (strs != null
          // && session.getPrincipal() != null
          && strs.length == 3) {
        log.info("strs is not null");
        String comment = strs[0];
        String commentUser = strs[1];
        String boardUser = strs[2];
        if (commentUser.equals(boardUser)) return;

        WebSocketSession boardSocketSession = userSessions.get(boardUser);
        
        log.info("unicasting test success");
        if (boardSocketSession != null) {
          log.info("boardSocketSession is not null");
          log.info("boardSocketSession : {}", boardSocketSession);
          log.info("boardSocketSession.getHandshakeHeaders() : {}", boardSocketSession.getHandshakeHeaders());
          log.info("boardSocketSession.getPrincipal().getName() : {}", boardSocketSession.getPrincipal().getName());
          TextMessage tm = new TextMessage(commentUser + "님이 댓글을 달았습니다! " + comment);
          boardSocketSession.sendMessage(tm);
        }
      }

    }
  }
  
  private String getId(WebSocketSession session) {
    // WebSocketServerSockJsSession
    // SockJsClient
    // UsernamePasswordAuthenticationToken.principal -> final field
    Principal principal = session.getPrincipal();
    log.info("principal : {}", principal);
    if (principal == null) {
      return session.getId();
    } else {
      return String.valueOf(principal.getName());
    }
  }

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    log.info("afterConnectionClosed >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> {} : {}", session, status);
    sessionList.remove(session);
	}
}