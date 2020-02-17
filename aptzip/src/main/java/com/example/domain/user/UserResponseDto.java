package com.example.domain.user;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
// @EqualsAndHashCode(of = "id") -> User에 구현되어 있음
public class UserResponseDto extends User {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private long id;
	private String email;
	private String phone;
	private String password;
	private String username;
	private String address;
	private String gender;
	private String introduction;
	private LocalDateTime signupDate;
	private int reported;
  private UserRole role;
  private Collection<UserPrivilege> privilege;
  private AptEntity apt;
  
  public UserResponseDto(
                    long id
                  , String username
                  , String password
                  , boolean enabled
                  , boolean accountNonExpired
                  , boolean credentialsNonExpired
                  , boolean accountNonLocked
                  , Collection<? extends GrantedAuthority> authorities
                  , String email
                  , String address
                  , String phone
                  , String gender
                  , String introduction
                  , LocalDateTime signupDate
                  , int reported
                  , UserRole role
                  , Collection<UserPrivilege> privilege
                  , AptEntity apt) {
    super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    this.id = id;
    this.username = username;
    this.password = password;
    this.email = email;
    this.phone = phone;
    this.address = address;
    this.gender = gender;
    this.introduction = introduction;
    this.signupDate = signupDate;
    this.reported = reported;
    this.role = role;
    this.privilege = privilege;
    this.apt = apt;
  }

  public AptzipUserEntity toEntity() {
    // return new AptzipUserEntity(id, email, phone, password, username, address, gender, introduction, signUpDate, reported, new AptzipRoleEntity(role.name()), apt);
    return AptzipUserEntity.builder()
                           .id(id)
                           .username(username)
                           .password(password)
                           .email(email)
                           .phone(phone)
                           .address(address)
                           .gender(gender)
                           .introduction(introduction)
                           .signupDate(signupDate)
                           .reported(reported)
                           .role(new AptzipRoleEntity(role.name()))
                           .apt(apt)
                           .build();
	}

}