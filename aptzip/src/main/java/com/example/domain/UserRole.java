package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {

  ADMIN("ADMIN"),
  MEMBER("MEMBER");

  private String value;
    
}