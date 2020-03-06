package com.markruler.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * https://d2.naver.com/helloworld/318732
 */
@Configuration
public class PasswordConfig {
	@Bean
  public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder(10);
  }
}