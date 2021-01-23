package com.markruler.aptzip.domain.user;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.domain.common.AptEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.social.security.SocialUserDetails;
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
@ToString(exclude = {"following", "follower"})
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto implements SocialUserDetails {
	private static final Long serialVersionUID = 1L;

	private Long id;

	@NotBlank(message = "메일을 입력해주세요.")
	@Email(message = "메일 양식을 지켜주세요.")
	private String email;

	@NotBlank(message = "닉네임을 입력해주세요.")
	@Pattern(regexp = "[a-zA-Z]{4,15}", message = "4~15자리의 영문자만 입력가능합니다")
	private String username;

	@NotBlank(message = "비밀번호를 입력해주세요") private String password;
	private String introduction;
	private LocalDateTime signUpDate;
	@PositiveOrZero private int reported;
	private List<BoardEntity> board;
	private AptzipRoleEntity role;
	private AptEntity apt;
	private List<UserFollowEntity> following;
	private List<UserFollowEntity> follower;
	private boolean isEnabled;
  private String providerId;
  private String providerUserId;

	public UserRequestDto(AptzipUserEntity user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.email = user.getEmail();
	}

	public AptzipUserEntity toEntity() {
		return new AptzipUserEntity(
			id,
			email,
			password,
			username,
			introduction,
			signUpDate,
			reported,
			board,
			role,
			apt,
			following,
			follower,
			isEnabled,
			providerId,
			providerUserId
		);
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

  // Social Account
	@Override
	public String getUserId() {
		return providerUserId;
	}

}