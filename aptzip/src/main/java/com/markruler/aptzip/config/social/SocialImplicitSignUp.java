package com.markruler.aptzip.config.social;

import com.markruler.aptzip.domain.user.AptzipUserEntity;
import com.markruler.aptzip.persistence.user.UserJpaRepository;

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
    // -> ConnectController
    UserProfile profile = connection.fetchUserProfile();
    log.info("profile : {}", profile);
    
    AptzipUserEntity user =
      AptzipUserEntity
        .builder()
        .username(profile.getUsername())
        .email(profile.getEmail())
        .build();
    userJpaRepository.save(user);
    
    log.info("user : {}", user);

		return user.getUsername();
	}
	
}