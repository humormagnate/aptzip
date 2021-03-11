package com.markruler.aptzip.service;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Deprecated(forRemoval = true)
@Slf4j
@RequiredArgsConstructor
// @Service
public class SocialUserAccountService implements SocialUserDetailsService {

	// private final UserDetailsService userDetailsService;
	private final UserAccountService userAccountService;

	// public SocialUsersAccountService(UserDetailsService userDetailsService) {
	// 	this.userDetailsService = userDetailsService;
	// }

  @Deprecated
	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException, DataAccessException {
    log.debug("social user account service : {}", userId);

		UserDetails userDetails = userAccountService.loadUserByUsername(userId);
		// return (UserRequestDto) userDetails;
		return null;
	}
}