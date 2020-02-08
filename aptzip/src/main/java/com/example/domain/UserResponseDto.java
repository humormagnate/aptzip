package com.example.domain;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class UserResponseDto {
	private String userId;
	private String email;
	private String phone;
	private String password;
	private String userName;
	private String address;
	private String gender;
	private String introduction;
	private Timestamp signUpDate;
	private int reported;
	
  public UserResponseDto(User user) {
    userId = user.getUserId();
    userName = user.getUserName();
    phone = user.getPhone();
    email = user.getEmail();
  }
  
//  private String toStringPhone(String phone1, String phone2, String phone3){
//    return phone1+"-"+phone2+"-"+phone3;
//  }
  
}
