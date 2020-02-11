package com.example.domain.user;

import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static com.example.domain.user.UserPrivilege.*;

@Slf4j
@Getter
@AllArgsConstructor
public enum UserRole {

  USER(Sets.newHashSet(COMMON_READ, BOARD_READ, BOARD_WRITE, NOTICE_READ)),
  ADMIN(Sets.newHashSet(COMMON_READ, BOARD_READ, BOARD_WRITE, NOTICE_READ, NOTICE_WRITE));

  private final Set<UserPrivilege> privileges;
  
  //https://www.baeldung.com/spring-security-granted-authority-vs-role
  public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
    Set<SimpleGrantedAuthority> privileges = getPrivileges().stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPrivileges()))
            .collect(Collectors.toSet());
    
    privileges.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

    log.info("privileges : " + privileges);

    return privileges;
  }
  
}