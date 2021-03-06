package com.markruler.aptzip.domain.user.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

import com.markruler.aptzip.domain.apartment.model.AptEntity;
import com.markruler.aptzip.domain.board.model.BoardEntity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
  @Pattern(regexp = "[a-zA-Z]{4,15}", message = "4~15자리의 영문과 숫자만 입력 가능합니다")
  private String username;

  @NotBlank(message = "비밀번호를 입력해주세요")
  private String password;

  private String introduction;
  private LocalDateTime signupDate;

  @PositiveOrZero
  private int reported;

  @OneToOne
  private AptEntity apt;

  private UserRole role;
  private List<BoardEntity> board;
  private List<UserFollowEntity> following;
  private List<UserFollowEntity> follower;
  private boolean enabled;

  public UserAccountRequestDto(UserAccountEntity user) {
    this.id = user.getId();
    this.username = user.getUsername();
    this.email = user.getEmail();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (UserRole.USER.equals(this.role)) {
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

  public UserAccountEntity toEntity() {
    // @formatter:off
    return new UserAccountEntity(
      this.id,
      this.email,
      this.username,
      this.password,
      this.introduction,
      this.signupDate,
      this.reported,
      this.apt,
      this.role == null ? UserRole.USER.name() : this.role.name(),
      this.board,
      this.following,
      this.follower,
      this.enabled
    );
    // @formatter:on
  }

  public static UserAccountRequestDto of(UserAccountEntity entity) {
    // @formatter:off
    return UserAccountRequestDto.builder()
      .id(entity.getId())
      .email(entity.getEmail())
      .password(entity.getPassword())
      .username(entity.getUsername())
      .introduction(entity.getIntroduction())
      .reported(entity.getReported())
      .board(entity.getBoard())
      .role(UserRole.valueOf(entity.getRole()))
      .apt(entity.getApt())
      .following(entity.getFollowing())
      .follower(entity.getFollower())
      .enabled(entity.isEnabled())
      .build();
    // @formatter:on
  }

}
