package com.example.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Getter;

@Getter
public class SecurityUser extends User {

	private static final String ROLE_PREFIX = "ROLE_";
  private User user;
  
	public SecurityUser(User user) {
		// super(user.getUserId(), user.getPassword(), makeGrantedAuthority(user.getRoles()));
		this.user = user;
	}

	private static List<GrantedAuthority> makeGrantedAuthority(List<UserRole> roles) {
		List<GrantedAuthority> list = new ArrayList<>();
		roles.forEach(role -> list.add(new SimpleGrantedAuthority(ROLE_PREFIX + role.getValue())));

		return list;
	}

}
