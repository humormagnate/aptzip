package com.markruler.aptzip.domain.user.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import com.markruler.aptzip.domain.apartment.model.AptEntity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(exclude = { "password", "following", "follower" })
public class UserAccountResponseDto extends User {

  private static final long serialVersionUID = 1L;

  private Long id;
  private String email;
  private String introduction;
  private LocalDateTime signupDate;
  private int reported;
  private UserRole role;
  private Collection<UserPrivilege> privilege;
  private AptEntity apt;
  private List<UserFollowEntity> following;
  private List<UserFollowEntity> follower;

  public UserAccountResponseDto(String username, String password, boolean enabled, boolean accountNonExpired,
      boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
    super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
  }

}
