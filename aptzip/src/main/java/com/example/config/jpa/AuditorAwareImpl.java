package com.example.config.jpa;

import java.util.Optional;

import com.example.domain.user.AptzipUserEntity;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

class AuditorAwareImpl implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(((AptzipUserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        // Can use Spring Security to return currently logged in user
        // return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()
    }
}