package com.markruler.aptzip.domain.user;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import com.markruler.aptzip.domain.apartment.AptEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {"following", "follower"})
public class UserResponseDto extends User {

  private static final long serialVersionUID = 1L;

  private long id;
	private String username;
	private String password;
	private String email;
	private String introduction;
	private LocalDateTime signupDate;
	private int reported;
  private UserRole role;
  private Collection<UserPrivilege> privilege;
  private AptEntity apt;
  private List<UserFollowEntity> following;
  private List<UserFollowEntity> follower;

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
                  , String introduction
                  , LocalDateTime signupDate
                  , int reported
                  , UserRole role
                  , Collection<UserPrivilege> privilege
                  , AptEntity apt,
                    List<UserFollowEntity> following,
                    List<UserFollowEntity> follower
                    ) {
    super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    this.id = id;
    this.username = username;
    this.password = password;
    this.email = email;
    this.introduction = introduction;
    this.signupDate = signupDate;
    this.reported = reported;
    this.role = role;
    this.privilege = privilege;
    this.apt = apt;
    this.following = following;
    this.follower = follower;
  }

  public AptzipUserEntity toEntity() {
    return AptzipUserEntity.builder()
                           .id(id)
                           .username(username)
                           .password(password)
                           .email(email)
                           .introduction(introduction)
                           .signupDate(signupDate)
                           .reported(reported)
                           .role(new AptzipRoleEntity(role.name()))
                           .apt(apt)
                           .following(following)
                           .follower(follower)
                           .build();
	}

}