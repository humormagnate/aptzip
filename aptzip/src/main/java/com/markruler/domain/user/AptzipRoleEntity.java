package com.markruler.domain.user;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "TB_ROLE")
public class AptzipRoleEntity implements GrantedAuthority {
  
  private static final long serialVersionUID = 1L;
  
  @Id
  private String role;

  // private String authority;

  @Override
  public String getAuthority() {
    return null;
  }

}