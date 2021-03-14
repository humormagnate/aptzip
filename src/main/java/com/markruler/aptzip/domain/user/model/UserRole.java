package com.markruler.aptzip.domain.user.model;

import static com.markruler.aptzip.domain.user.model.UserPrivilege.BOARD_READ;
import static com.markruler.aptzip.domain.user.model.UserPrivilege.BOARD_WRITE;
import static com.markruler.aptzip.domain.user.model.UserPrivilege.COMMON_READ;
import static com.markruler.aptzip.domain.user.model.UserPrivilege.NOTICE_READ;
import static com.markruler.aptzip.domain.user.model.UserPrivilege.NOTICE_WRITE;

import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {

  USER(Sets.newHashSet(COMMON_READ, BOARD_READ, BOARD_WRITE, NOTICE_READ)),
  ADMIN(Sets.newHashSet(COMMON_READ, BOARD_READ, BOARD_WRITE, NOTICE_READ, NOTICE_WRITE));

  private Set<UserPrivilege> privileges;

  public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
    Logger log = LoggerFactory.getLogger(UserRole.class);

    Set<SimpleGrantedAuthority> grantedAuthorities = getPrivileges().stream()
        .map(permission -> new SimpleGrantedAuthority(permission.getPrivileges())).collect(Collectors.toSet());

    grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    log.debug("grantedAuthorities : {}", grantedAuthorities);
    return grantedAuthorities;
  }

}
