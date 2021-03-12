package com.markruler.aptzip.domain.user;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import com.markruler.aptzip.domain.apartment.AptEntity;

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

  public UserAccountEntity toEntity() {
    return UserAccountEntity.builder()
    // @formatter:off
      .id(id)
      .username(super.getUsername())
      .password(super.getPassword())
      .email(email)
      .introduction(introduction)
      .signupDate(signupDate)
      .reported(reported)
      .role(role.name())
      .apt(apt)
      .following(following)
      .follower(follower)
      .build();
    // @formatter:on
  }

}
