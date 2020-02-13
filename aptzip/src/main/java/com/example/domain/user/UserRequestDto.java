package com.example.domain.user;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

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
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto implements UserDetails {
	
	private static final long serialVersionUID = 1L;

	private long id;

	@NotBlank(message = "메일을 입력해주세요.")
	@Email(message = "메일 양식을 지켜주세요.")
	private String email;

	// @NotBlank(message = "전화번호를 작성해주세요.")
	// @Pattern(regexp = "[0-9]{10,11}", message = "10~11자리의 숫자만 입력가능합니다")
	private String phone;

	@NotBlank(message = "닉네임을 입력해주세요.")
	@Pattern(regexp = "[a-zA-Z]{4,15}", message = "4~15자리의 영문자만 입력가능합니다")
	private String username;

	@NotBlank(message = "비밀번호를 입력해주세요")
	private String password;

	private String address;
	private String gender;
	private String introduction;
	private LocalDateTime signUpDate;

	@PositiveOrZero
	private int reported;
	private AptzipRoleEntity role;
	private AptEntity apt;

	public UserRequestDto(AptzipUserEntity user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.email = user.getEmail();
	}

	public AptzipUserEntity toEntity() {
		// User랑 컬럼 순서가 다르면 다르게 삽입됨
		return new AptzipUserEntity(id, email, phone, password, username, address, gender, introduction, signUpDate, reported, role, apt);
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