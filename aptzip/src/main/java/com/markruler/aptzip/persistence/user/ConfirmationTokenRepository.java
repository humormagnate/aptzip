package com.markruler.aptzip.persistence.user;

import com.markruler.aptzip.domain.user.ConfirmationToken;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String> {
  
  ConfirmationToken findByConfirmationToken(String confirmationToken);
  
}