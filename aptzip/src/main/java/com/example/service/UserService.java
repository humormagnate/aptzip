package com.example.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.user.AptzipUserEntity;
import com.example.domain.user.UserRequestDto;
import com.example.domain.user.UserResponseDto;
import com.example.domain.user.UserRole;
import com.example.persistence.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Transactional
  public AptzipUserEntity save(UserRequestDto userRequestDto) {
    // log.info("UserService : " + userRequestDto);
    userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
    // log.info("UserService : " + userRequestDto);
    // log.info("UserService.toEntity() : " + userRequestDto.toEntity());
    return userRepository.save(userRequestDto.toEntity());
  }

  @Transactional(readOnly = true)
  public List<UserResponseDto> findAll() {
    return userRepository.findAll().stream()
                              .map(UserResponseDto::new)
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
    // List<GrantedAuthority> authorities = new ArrayList<>();
    StringBuilder authority = new StringBuilder();

    // authorization
    if (user.getRole() != null && user.getRole().getRole().equals(UserRole.USER.name())) {
      // UserBuilder creates GrantedAuthority via roles()
      // authorities.add(new SimpleGrantedAuthority(UserRole.USER.name()));
      authority.append(UserRole.USER.name());
    } else {
      // authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.name()));
      authority.append(UserRole.ADMIN.name());
    }
    
    // roles.forEach( role -> list.add(new SimpleGrantedAuthority("ROLE_" + role.getRole())));

    // UserDetails returned to Principal
    return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(authority.toString()) // append "ROLE_" prefix
                .build();
  }
  
}
