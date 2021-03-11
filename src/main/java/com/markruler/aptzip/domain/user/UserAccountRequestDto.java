package com.markruler.aptzip.domain.user;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

import com.markruler.aptzip.domain.apartment.AptEntity;
import com.markruler.aptzip.domain.board.BoardEntity;

import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.social.security.SocialUserDetails;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// TODO validation
@Getter
@Setter
@Builder
@ToString(exclude = { "password", "following", "follower" })
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountRequestDto implements UserDetails {
  private static final long serialVersionUID = 7811067135242824850L;

  private Long id;

  @NotBlank(message = "메일을 입력해주세요.")
  @Email(message = "메일 양식을 지켜주세요.")
  private String email;

  @NotBlank(message = "닉네임을 입력해주세요.")
  @Pattern(regexp = "[a-zA-Z]{4,15}", message = "4~15자리의 영문자만 입력가능합니다")
  private String username;

  @NotBlank(message = "비밀번호를 입력해주세요")
  private String password;

  @PositiveOrZero
  private int reported;

  @OneToOne
  private transient AptEntity apt;

  private String introduction;
  private LocalDateTime signUpDate;
  private AptzipRoleEntity role;
  private transient List<BoardEntity> board;
  private transient List<UserFollowEntity> following;
  private transient List<UserFollowEntity> follower;
  private boolean isEnabled;
  // private String providerId;
  // private String providerUserId;

  public UserAccountRequestDto(UserAccountEntity user) {
    this.id = user.getId();
    this.username = user.getUsername();
    this.email = user.getEmail();
  }

  public UserAccountEntity toEntity() {
    // @formatter:off
    return UserAccountEntity.builder()
      .id(this.id == null ? null : this.id)
      .email(this.email)
      .password(this.password)
      .username(this.username)
      .introduction(this.introduction)
      .reported(this.reported)
      .board(this.board)
      .role(this.role)
      .apt(this.apt)
      .following(this.following)
      .follower(this.follower)
      .isEnabled(this.isEnabled)
      .build();
    // @formatter:on
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (UserRole.USER.name().equals(role.getRole())) {
      return UserRole.USER.getGrantedAuthorities();
    } else {
      return UserRole.ADMIN.getGrantedAuthorities();
    }
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
