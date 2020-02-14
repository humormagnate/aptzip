package com.example.config.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// spring message로 변경할 것
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ErrorCode {

  NOT_NULL("ERROR_CODE_0001", "필수값이 누락되었습니다"),
  MIN_VALUE("ERROR_CODE_0002", "최소값보다 커야 합니다.");

  private String code;
  private String description;
}