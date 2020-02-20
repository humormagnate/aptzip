package com.example.config.websocket.stateless;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketMessageBrokerConfig implements WebSocketMessageBrokerConfigurer {

	/**
	 * Register STOMP endpoints mapping each to a specific URL
	 * and (optionally) enabling and configuring SockJS fallback options.
	 */
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws/message")
						.setAllowedOrigins("*")
						.withSockJS();
  }
  
	/**
	 * Configure message broker options.
	 */
	public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.setApplicationDestinationPrefixes("/sub");
		registry.enableSimpleBroker("/topic", "/queue");
		// registry.enableStompBrokerRelay("/topic", "/queue")
	}
}