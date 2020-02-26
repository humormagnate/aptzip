package com.example.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.domain.user.AptzipUserEntity;
import com.example.domain.user.UserRequestDto;
import com.example.domain.user.UserResponseDto;
import com.example.domain.user.UserRole;
import com.example.persistence.user.UserJpaRepository;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserAccountService implements UserDetailsService {

  private final UserJpaRepository userJpaRepository;

  
  // 제약조건 : Controller 에서 Auth를 점검할 때, UserResponseDto(User 구현체)로 받아야 함.
  //            @AuthenticationPrincipal User user => (변경 후) @AuthenticationPrincipal UserResponseDto user
  boolean enabled = true;
  boolean accountNonExpired = true;
  boolean credentialsNonExpired = true;
  boolean accountNonLocked = true;

  // @Transactional(rollbackFor = Exception.class)
  // public void save(UserRequestDto userRequestDto) {
  //   log.info("save user/////////////////////////////////////////////////////");
  //   userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
  //   userRequestDto.setRole(new AptzipRoleEntity(UserRole.USER.name()));
  //   AptzipUserEntity entity = userRequestDto.toEntity();
  //   userJpaRepo.save(entity);
  // }

  @Transactional(rollbackFor = Exception.class)
  public void save(AptzipUserEntity user) {
    log.info("userAccountService save user/////////////////////////////////////////////////////");
    userJpaRepository.save(user);
  }

  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @Transactional(readOnly = true)
  public List<UserRequestDto> findAll() {
    return userJpaRepository.findAll().stream()
                            .map(UserRequestDto::new)
                            .collect(Collectors.toList());
  }
  
  // UsernamePasswordAuthenticationFilter -> DaoAuthenticationProvider -> UserDetailsService -> loadUserByUsername
  // https://www.baeldung.com/spring-security-extra-login-fields


  // https://lemontia.tistory.com/602
  // 해당기능을 사용할 시 외부로그인 연동(네이버나 페이스북 로그인 등)시 세션처리에 문제를 겪을 수 있어 추천드리지 않습니다.
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    
    log.info("===============================UserService-loadUserByUsername start=====================================");
    log.info("username : {}", username);

    // return type이 Optional<T>
    Optional<AptzipUserEntity> userEntityWrapper = userJpaRepository.findByUsername(username);
    // Optional<T>에서 get()을 통해 T 반환
    AptzipUserEntity user = userEntityWrapper.get();
    
    log.info("===============================UserService-loadUserByUsername get=====================================");
    log.info("user : {}", user);
    // -> could not initialize proxy [com.example.domain.user.AptEntity#1] - no Session
    // AptEntity 에 대한 FetchType을 LAZY로 해서 그럼...EAGER로 ㄱㄱ

    // If the user does not exist
    if (user == null) {
      throw new UsernameNotFoundException("Not found " + username);
    }

    if (!user.isEnabled()) {
      // AuthenticationException : abstract class -> Cannot instantiate the type ...
      // throw new AuthenticationException("asd");
      throw new AuthenticationCredentialsNotFoundException("This account requires email verification.");
    }

    // AptzipRoleEntity role = new AptzipRoleEntity(user.getRole().getRole());
    
    UserResponseDto urd = new UserResponseDto(
                                user.getId(),
                                user.getUsername(),
                                user.getPassword(),
                                enabled,
                                accountNonExpired,
                                credentialsNonExpired,
                                accountNonLocked,
                                UserRole.USER.getGrantedAuthorities(),
                                user.getEmail(),
                                user.getIntroduction(),
                                user.getSignupDate(),
                                user.getReported(),
                                UserRole.USER,
                                UserRole.USER.getPrivileges(),
                                user.getApt(),
                                user.getFollowing(),
                                user.getFollower()
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
    log.info("urd : {}", urd);

    return urd;
  }

  public AptzipUserEntity findById(Long id) {
    // AptzipUserEntity optional optional = userJpaRepository.findById(id);
    // Optional<AptzipUserEntity> user = Optional.ofNullable(optional);
    // AptzipUserEntity user = optional.get();
    // return Optional.ofNullable(findById(id)).filter(value -> value != null).map();
    return userJpaRepository.findById(id).orElse(new AptzipUserEntity());
  }

  public void delete(Long id) {
    userJpaRepository.deleteById(id);
  }

  public void updatePassword(UserRequestDto user) {
    userJpaRepository.updatePasswordById(user.getPassword(), user.getId());
  }

}