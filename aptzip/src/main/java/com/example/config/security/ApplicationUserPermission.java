package com.example.config.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ApplicationUserPermission {
	USER_READ("user:read"),
  USER_WRITE("user:write"),
  BOARD_READ("board:read"),
  BOARD_WRITE("board:write");

  private final String permission;

}
