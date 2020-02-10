package com.example.domain.user;

import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.example.domain.user.UserPermission.*;

@Getter
@AllArgsConstructor
public enum UserRole {

  USER(Sets.newHashSet(COMMON_READ, BOARD_READ, BOARD_WRITE, NOTICE_READ)),
  ADMIN(Sets.newHashSet(COMMON_READ, BOARD_READ, BOARD_WRITE, NOTICE_READ, NOTICE_WRITE));

  private final Set<UserPermission> permissions;
  
  public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
    Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
            .collect(Collectors.toSet());
    permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return permissions;
  }
  
}