package com.markruler.aptzip.service;

import com.markruler.aptzip.domain.user.ConfirmationToken;
import com.markruler.aptzip.domain.user.UserAccountEntity;
import com.markruler.aptzip.persistence.user.ConfirmationTokenRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ConfirmationService {
  private final ConfirmationTokenRepository confirmationTokenRepository;

  public ConfirmationToken save(UserAccountEntity user) {
    ConfirmationToken confirmationToken = new ConfirmationToken(user);
    return confirmationTokenRepository.save(confirmationToken);
  }

  public ConfirmationToken findByToken(String token) {
    return confirmationTokenRepository.findByToken(token);
  }
}
