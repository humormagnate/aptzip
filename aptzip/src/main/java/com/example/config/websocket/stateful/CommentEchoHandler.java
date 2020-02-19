package com.example.config.websocket.stateful;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.domain.user.UserResponseDto;
import com.example.service.UserService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommentEchoHandler extends TextWebSocketHandler {

  // all user
  private final List<WebSocketSession> sessionList = new ArrayList<>();

  // specific user
  private final Map<String, WebSocketSession> userSessions = new HashMap<>();

  // Invoked after WebSocket negotiation has succeeded
  // and the WebSocket connection is opened and ready for use.
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    log.info("afterConnectionEstablished >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> {}", session);
    sessionList.add(session);
    String senderId = getId(session);
    userSessions.put(senderId, session);
	}

  // Invoked when a new WebSocket message arrives.
  @Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    log.info("handleTextMessage >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>{}:{}", session, message);
    String senderId = getId(session);

    // broadcasting
    // for (WebSocketSession webSocketSession : sessionList) {
    //   webSocketSession.sendMessage(new TextMessage(senderId + " : " + message.getPayload()));
    //   log.info("broadcasting test success");
    // }
    
    // unicasting?
    // protocol : 댓글, 작성자, 게시글작성자, 게시글번호 (comment, comment_user_id, board_user_id, board_id)
    String msg = message.getPayload();
    log.info("payload {}", msg);

    if (StringUtils.isNotEmpty(msg)) {
      log.info("unicasting test success");
      String[] strs = msg.split(",");
      if (strs != null && strs.length == 3) {
        log.info("strs is not null");
        String comment = strs[0];
        String commentUser = strs[1];
        String boardUser = strs[2];
        if (commentUser.equals(boardUser)) return;
        log.info("{}", strs[1]);
        WebSocketSession boardSocketSession = userSessions.get(boardUser);
        if (boardSocketSession != null) {
          log.info("boardSocketSession : {}", boardSocketSession);
          log.info("boardSocketSession.getHandshakeHeaders() : {}", boardSocketSession.getHandshakeHeaders());
          log.info("boardSocketSession.getPrincipal().getName() : {}", boardSocketSession.getPrincipal().getName());
        }
        // if ("comment".equals(comment) && boardSocketSession != null) {
        
        if (boardSocketSession != null) {
          log.info("boardSocketSession is not null");
          TextMessage tm = new TextMessage(commentUser + "님이 댓글을 달았습니다! " + comment);
          boardSocketSession.sendMessage(tm);
        }
      }
    }
  }
  
  private String getId(WebSocketSession session) {
    Map<String, Object> httpSession = session.getAttributes();
    
    // if (!(auth instanceof AnonymousAuthenticationToken)) {
    //   String currentUserName = auth.getName();
    //   return currentUserName;
    // }
    
    // WebSocketServerSockJsSession
    // SockJsClient
    // UsernamePasswordAuthenticationToken.principal -> final field
    Principal principal = session.getPrincipal();
    // log.info("principal : {}", principal);
    // log.info("principal.getName() : {}", principal.getName());
    
    // Authentication auth = (Authentication)session.getPrincipal();
    // log.info("auth : {}", auth);
    // log.info("auth.getName() : {}", auth.getName());

    // log.info("user service : {}", service);
    // UserDetailsByNameServiceWrapper<Authentication> wrap = new UserDetailsByNameServiceWrapper<>(service);
    // UserResponseDto user = (UserResponseDto)wrap.loadUserDetails(auth);
    // log.info("user : {}", user);

    // UserService userService = new UserService();
    // UserResponseDto user = (UserResponseDto)userService.loadUserByUsername(auth.getName());
    // log.info("user : {}", user);
    
    if (principal == null) {
      return session.getId();
    } else {
      return String.valueOf(principal.getName());
    }
  }

  // Invoked after the WebSocket connection has been closed by either side,
  // or after a transport error has occurred.
  //
  // Although the session may technically still be open, depending on the underlying implementation,
  // sending messages at this point is discouraged and most likely will not succeed.
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    log.info("afterConnectionClosed >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> {} : {}", session, status);
    sessionList.remove(session);
	}
}