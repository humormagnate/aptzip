package com.example.service;

import java.security.cert.PKIXRevocationChecker.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.User;
import com.example.domain.UserRequestDto;
import com.example.domain.UserResponseDto;
import com.example.domain.UserRole;
import com.example.persistence.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Transactional
  public User save(UserRequestDto userRequestDto) {
    userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
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
    Optional<User> userEntityWrapper = userRepository.findByEmail(email);
    User user = userEntityWrapper.get();
    
    /* 존재하지 않는 사용자일 경우 */
    if (user == null) {
        throw new UsernameNotFoundException("Not found " + email);
    }

    List<GrantedAuthority> authorities = new ArrayList<>();

    if ((user.getEmail()).equals(email)) {
        authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
    } else {
        authorities.add(new SimpleGrantedAuthority(UserRole.MEMBER.getValue()));
    }
    
    // role.forEach( role -> list.add(new SimpleGrantedAuthority("ROLE_" + role.getRole())));

    return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
  }
  
  
}
