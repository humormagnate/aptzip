package com.example.domain.user;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class UserResponseDto {
	private long id;
	private String email;
	private String phone;
	private String password;
	private String username;
	private String address;
	private String gender;
	private String introduction;
	private Timestamp signUpDate;
	private int reported;
	private String role;
	
  public UserResponseDto(AptzipUserEntity user) {
    this.id = user.getId();
    this.username = user.getUsername();
    this.phone = user.getPhone();
    this.email = user.getEmail();
  }
  
//  private String toStringPhone(String phone1, String phone2, String phone3){
//    return phone1+"-"+phone2+"-"+phone3;
//  }
  
}
