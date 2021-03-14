package com.markruler.aptzip.domain.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserPrivilege {
  COMMON_READ("common:read"),
  BOARD_READ("board:read"),
  NOTICE_READ("notice:read"),
  BOARD_WRITE("board:write"),
  NOTICE_WRITE("notice:write");

  private final String privileges;
}
