package com.example.config.security;

import com.example.domain.user.AptzipUserEntity;
import com.example.persistence.user.UserJpaRepository;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SocialImplicitSignUp implements ConnectionSignUp {

	private final UserJpaRepository userJpaRepository;
	
	@Override
	public String execute(Connection<?> connection) {
    log.info("================================== SocialImplicitSignUp execute ==================================");

    // (#100) Tried accessing nonexisting field (bio) on node type (User)
    // -> access_token problem
    // 일단 ConnectController로 구현하자
    UserProfile profile = connection.fetchUserProfile();
    log.info("profile : {}", profile);
    
    AptzipUserEntity user =
      AptzipUserEntity
        .builder()
        .username(profile.getUsername())
        .email(profile.getEmail())
        .build();
      // connection.getImageUrl()
    userJpaRepository.save(user);
    
    log.info("user : {}", user);

		return user.getUsername();
	}
	
}