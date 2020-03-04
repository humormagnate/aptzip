package com.markruler.persistence.user;

import com.markruler.domain.user.ConfirmationToken;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String> {
  
  ConfirmationToken findByConfirmationToken(String confirmationToken);
  
}