package com.example.domain.user;

import java.time.LocalDateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

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
public class UserRequestDto {
	
	private long id;

	@NotBlank(message = "메일을 작성해주세요.")
	@Email(message = "메일의 양식을 지켜주세요.")
	private String email;

	//@NotBlank(message = "전화번호를 작성해주세요.")
	//@Pattern(regexp = "[0-9]{10,11}", message = "10~11자리의 숫자만 입력가능합니다")
	private String phone;

	@NotBlank(message = "이름을 작성해주세요.")
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
	// private List<UserRole> roles;
	// private UserRole roles;
	private AptzipRoleEntity role;

	public AptzipUserEntity toEntity() {
		// User랑 컬럼 순서가 다르면 다르게 삽입됨
		return new AptzipUserEntity(id, email, phone, password, username, address, gender, introduction, signUpDate, reported, new AptzipRoleEntity("USER"));
	}

	// private String[] parsePhone() {
	// 	String[] phones = new String[3];
	// 	int mid = phone.length() == 10 ? 7 : 8;
	// 	phones[0] = phone.substring(0, 3);
	// 	phones[1] = phone.substring(4, mid);
	// 	phones[2] = phone.substring(mid, phone.length() - 1);
	// 	return phones;
	// }
}