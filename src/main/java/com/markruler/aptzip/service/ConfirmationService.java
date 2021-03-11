package com.markruler.aptzip.service;

import com.markruler.aptzip.domain.user.UserAccountEntity;
import com.markruler.aptzip.domain.user.ConfirmationToken;
import com.markruler.aptzip.persistence.user.ConfirmationTokenRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Deprecated(forRemoval = false)
@Service
@RequiredArgsConstructor
public class ConfirmationService {
  private final ConfirmationTokenRepository confirmationTokenRepository;

  public ConfirmationToken createToken(UserAccountEntity user) {
    ConfirmationToken confirmationToken = new ConfirmationToken(user);
    return confirmationTokenRepository.save(confirmationToken);
  }

  public ConfirmationToken findToken(String token) {
    return confirmationTokenRepository.findByConfirmationToken(token);
  }
}
