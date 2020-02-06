package com.example.security;

import lombok.Getter;

@Getter
public enum ApplicationUserPermission {
	USER_READ("user:read"),
  USER_WRITE("user:write"),
  BOARD_READ("board:read"),
  BOARD_WRITE("board:write");

  private final String permission;

  ApplicationUserPermission(String permission) {
      this.permission = permission;
  }
  
}
