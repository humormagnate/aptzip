package com.example.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// https://d2.naver.com/helloworld/318732
// 안전한 패스워드 저장 (one-way hash function)
@Configuration
public class PasswordConfig {
	@Bean
  public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder(10);
  }
}