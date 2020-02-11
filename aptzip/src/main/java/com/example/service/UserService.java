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
    // log.info("UserService : " + userRequestDto);
    userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
    // log.info("UserService : " + userRequestDto);
    // log.info("UserService.toEntity() : " + userRequestDto.toEntity());
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

    // return type이 Optional<T>
    Optional<AptzipUserEntity> userEntityWrapper = userRepository.findByEmail(email);
    // Optional<T>에서 get()을 통해 T 반환
    AptzipUserEntity user = userEntityWrapper.get();
    
    // If the user does not exist
    if (user == null) {
      throw new UsernameNotFoundException("Not found " + email);
    }

    log.info("===============================UserService-loadUserByUsername=====================================");
    log.info("email : " + email);
    log.info("user : " + user);
    
    
    UserResponseDto urd = new UserResponseDto(

                                user.getUsername()
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
    
    
    // List<GrantedAuthority> authorities = new ArrayList<>();
    // StringBuilder authority = new StringBuilder();
    
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
    
    // log.info("role : " + authority.toString());
    log.info("===============================UserService-loadUserByUsername return=====================================");
    // UserDetails returned to Principal
    
    // return User.builder()
    //             .username(user.getUsername())
    //             .password(user.getPassword())
    //             .roles(authority.toString()) // append "ROLE_" prefix
    //             .build();


    return urd;
  }

  public AptzipUserEntity findOne(Long id) {
    return null;
  }
  
}
