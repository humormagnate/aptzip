package com.example.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserPermission {
  COMMON_READ("common:read"),
  BOARD_READ("board:read"),
  NOTICE_READ("notice:read"),
  BOARD_WRITE("board:write"),
  NOTICE_WRITE("notice:write");

  private final String permission;
}
