package com.example.domain;

import java.sql.Timestamp;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Builder.Default;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
	
	private int userId;

	@NotBlank(message = "메일을 작성해주세요.")
	@Email(message = "메일의 양식을 지켜주세요.")
	private String email;

	//@NotBlank(message = "전화번호를 작성해주세요.")
	//@Pattern(regexp = "[0-9]{10,11}", message = "10~11자리의 숫자만 입력가능합니다")
	private String phone;
	
	@NotBlank(message = "비밀번호를 입력해주세요")
	private String password;

	@NotBlank(message = "이름을 작성해주세요.")
	@Pattern(regexp = "[가-힣]{2,10}", message = "2~10자리의 한글만 입력가능합니다")
	private String userName;

	private String address;
	private String gender;
	private String introduction;
	private Timestamp signUpDate;
	private int reported;
	private List<UserRole> roles;

	public User toEntity() {
		// String[] phones = parsePhone();
		return new User(userId, email, phone, password, userName, address, gender, introduction, new Timestamp(System.currentTimeMillis()), reported);
		// return new User(userId, email, phone, password, userName, address, gender, introduction, new Timestamp(System.currentTimeMillis()), reported, roles);
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
