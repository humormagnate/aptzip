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
	private LocalDateTime signUpDate;
	private int reported;
  private UserRole role;
  private Collection<UserPrivilege> privilege;
  
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
                  , LocalDateTime signUpDate
                  , int reported
                  , UserRole role
                  , Collection<UserPrivilege> privilege) {
    super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    this.id = id;
    this.username = username;
    this.password = password;
    this.email = email;
    this.phone = phone;
    this.address = address;
    this.gender = gender;
    this.introduction = introduction;
    this.signUpDate = signUpDate;
    this.reported = reported;
    this.role = role;
    this.privilege = privilege;

  }

  // public UserResponseDto(AptzipUserEntity user) {
    //   this.id = user.getId();
    //   this.username = user.getUsername();
    //   this.phone = user.getPhone();
    //   this.email = user.getEmail();
    // }
    
    //  private String toStringPhone(String phone1, String phone2, String phone3){
      //    return phone1+"-"+phone2+"-"+phone3;
      //  }
      
    }