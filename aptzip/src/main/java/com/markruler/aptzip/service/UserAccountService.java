package com.markruler.aptzip.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.markruler.aptzip.domain.user.AptzipUserEntity;
import com.markruler.aptzip.domain.user.UserRequestDto;
import com.markruler.aptzip.domain.user.UserResponseDto;
import com.markruler.aptzip.domain.user.UserRole;
import com.markruler.aptzip.persistence.user.UserJpaRepository;

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

  boolean enabled = true;
  boolean accountNonExpired = true;
  boolean credentialsNonExpired = true;
  boolean accountNonLocked = true;

  @Transactional(rollbackFor = Exception.class)
  public void save(AptzipUserEntity user) {
    log.info("=============================== userAccountService save user ===============================");
    userJpaRepository.save(user);
  }

  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @Transactional(readOnly = true)
  public List<UserRequestDto> findAll() {
    return userJpaRepository.findAll().stream()
                            .map(UserRequestDto::new)
                            .collect(Collectors.toList());
  }
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    
    log.info("=============================== UserService-loadUserByUsername start =====================================");
    log.info("username : {}", username);

    Optional<AptzipUserEntity> userEntityWrapper = userJpaRepository.findByUsername(username);
    AptzipUserEntity user = userEntityWrapper.get();
    
    log.info("=============================== UserService-loadUserByUsername get =====================================");
    log.info("user : {}", user);

    if (user == null) {
      throw new UsernameNotFoundException("Not found " + username);
    }

    if (!user.isEnabled()) {
      throw new AuthenticationCredentialsNotFoundException("This account requires email verification or disabled.");
    }
    
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
      urd.setRole(UserRole.USER);
      urd.setPrivilege(UserRole.USER.getPrivileges());
    } else {
      urd.setRole(UserRole.ADMIN);
      urd.setPrivilege(UserRole.ADMIN.getPrivileges());
    }

    log.info("=============================== UserService-loadUserByUsername return =====================================");
    log.info("urd : {}", urd);
    return urd;
  }

  public AptzipUserEntity findById(Long id) {
    return userJpaRepository.findById(id).orElse(new AptzipUserEntity());
  }

  public void disabledUser(Long id) {
    userJpaRepository.disabledUserById(id);
  }

  public void updatePassword(UserRequestDto user) {
    userJpaRepository.updatePasswordById(user.getPassword(), user.getId());
  }

}
