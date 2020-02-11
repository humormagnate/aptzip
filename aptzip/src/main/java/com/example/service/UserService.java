package com.example.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.domain.user.AptzipUserEntity;
import com.example.domain.user.UserRequestDto;
import com.example.domain.user.UserResponseDto;
import com.example.domain.user.UserRole;
import com.example.persistence.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  
  // 제약조건 : Controller 에서 Auth를 점검할 때, UserResponseDto(User 구현체)로 받아야 함.
  //            @AuthenticationPrincipal User user => (변경 후) @AuthenticationPrincipal UserResponseDto user
  boolean enabled = true;
  boolean accountNonExpired = true;
  boolean credentialsNonExpired = true;
  boolean accountNonLocked = true;

  @Transactional
  public AptzipUserEntity save(UserRequestDto userRequestDto) {
    userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
    return userRepository.save(userRequestDto.toEntity());
  }

  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @Transactional(readOnly = true)
  public List<UserRequestDto> findAll() {
    return userRepository.findAll().stream()
                          .map(UserRequestDto::new)
                          .collect(Collectors.toList());
  }
  
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    
    log.info("===============================UserService-loadUserByUsername start=====================================");
    // return type이 Optional<T>
    Optional<AptzipUserEntity> userEntityWrapper = userRepository.findByEmail(email);
    // Optional<T>에서 get()을 통해 T 반환
    AptzipUserEntity user = userEntityWrapper.get();
    
    log.info("===============================UserService-loadUserByUsername get=====================================");
    log.info("user : " + user);
    // If the user does not exist
    if (user == null) {
      throw new UsernameNotFoundException("Not found " + email);
    }

    // https://otrodevym.tistory.com/entry/Spring-Security-%EC%A0%95%EB%A6%AC-4-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EC%8B%A4%ED%8C%A8-%ED%9B%84-%EC%B2%98%EB%A6%AC
    // 가끔 InternalAuthenticationServiceException? 존재 하지 않는 아이디일 경우

    // Spring Security FilterChain 공부하기 (14개, loggin level = debug로 하면 볼 수 있음)

    // InitializeUserDetailsBeanManagerConfigurer
    // AbstractUserDetailsAuthenticationProvider
    // AuthenticationManagerBuilder
    // TokenBasedRememberMeServices
    // UsernamePasswordAuthenticationToken
    // SecurityContextHolderAwareRequestFilter
    
    UserResponseDto urd = new UserResponseDto(
                                user.getId()
                              , user.getUsername()
                              , user.getPassword()
                              , enabled
                              , accountNonExpired
                              , credentialsNonExpired
                              , accountNonLocked
                              , UserRole.USER.getGrantedAuthorities()
                              , user.getEmail()
                              , user.getAddress()
                              , user.getPhone()
                              , user.getGender()
                              , user.getIntroduction()
                              , user.getSignUpDate()
                              , user.getReported()
                              , null
                              , UserRole.USER.getPrivileges()
                              
                              );
    
    // authorization
    if (user.getRole() != null && user.getRole().getRole().equals(UserRole.USER.name())) {
      // UserBuilder creates GrantedAuthority via roles()
      // authorities.add(new SimpleGrantedAuthority(UserRole.USER.name()));
      urd.setRole(UserRole.USER);
      urd.setPrivilege(UserRole.USER.getPrivileges());
    } else {
      urd.setRole(UserRole.ADMIN);
      urd.setPrivilege(UserRole.ADMIN.getPrivileges());
    }

    // roles.forEach( role -> list.add(new SimpleGrantedAuthority("ROLE_" + role.getRole())));
    log.info("===============================UserService-loadUserByUsername return=====================================");
    log.info("urd : " + urd);

    return urd;
  }

  public AptzipUserEntity findOne(Long id) {
    return null;
  }
  
}
