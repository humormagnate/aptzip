package com.example.service;

import com.example.domain.user.UserRequestDto;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class SocialUserAccountService implements SocialUserDetailsService {

	// private final UserDetailsService userDetailsService;
	private final UserAccountService userAccountService;

	// public SocialUsersAccountService(UserDetailsService userDetailsService) {
	// 	this.userDetailsService = userDetailsService;
	// }

	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException, DataAccessException {
    log.info("social user account service : {}", userId);
    
		UserDetails userDetails = userAccountService.loadUserByUsername(userId);
		return (UserRequestDto) userDetails;
	}
}