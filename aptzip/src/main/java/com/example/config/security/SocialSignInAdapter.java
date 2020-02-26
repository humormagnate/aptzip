package com.example.config.security;

import javax.servlet.http.HttpServletRequest;

import com.example.domain.user.AptzipUserEntity;
import com.example.persistence.user.UserJpaRepository;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SocialSignInAdapter implements SignInAdapter {

	private final UserJpaRepository userJpaRepository;

	@Override
	public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {
		log.info("===================================== social signin adapter =====================================");
		UserProfile profile = connection.fetchUserProfile();

		log.info("UserProfile : {}", profile);
		
		AptzipUserEntity user = userJpaRepository.findByEmailIgnoreCase(profile.getEmail()).orElse(null);
		if (user != null) {
			log.info("social signin user is not null : {}", user);
			HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();
			servletRequest.getSession().setAttribute("loggedUser", user);
			return "/";
		} else {
			log.info("social signin user is null : {}", user);
			return "/error";
		}
	}

}