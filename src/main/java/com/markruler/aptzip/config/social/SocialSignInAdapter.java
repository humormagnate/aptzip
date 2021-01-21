package com.markruler.aptzip.config.social;

import javax.servlet.http.HttpServletRequest;

import com.markruler.aptzip.domain.user.AptzipUserEntity;
import com.markruler.aptzip.persistence.user.UserJpaRepository;

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
		log.debug("===================================== social signin adapter =====================================");
		UserProfile profile = connection.fetchUserProfile();

		log.debug("UserProfile : {}", profile);
		
		AptzipUserEntity user = userJpaRepository.findByEmailIgnoreCase(profile.getEmail()).orElse(null);
		if (user != null) {
			log.debug("social signin user is not null : {}", user);
			HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();
			servletRequest.getSession().setAttribute("loggedUser", user);
			return "/";
		} else {
			log.debug("social signin user is null : {}", user);
			return "/error";
		}
	}

}