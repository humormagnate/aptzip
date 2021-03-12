package com.markruler.aptzip.domain.user;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_ROLE")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AptzipRoleEntity implements GrantedAuthority {
  private static final long serialVersionUID = 1L;

  @Id
  private String role;

  @Override
  public String getAuthority() {
    // TODO: implement GrantedAuthority
    return null;
  }

}