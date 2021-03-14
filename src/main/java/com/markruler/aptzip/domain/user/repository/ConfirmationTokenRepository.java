package com.markruler.aptzip.domain.user.repository;

import com.markruler.aptzip.domain.user.model.ConfirmationToken;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String> {

  ConfirmationToken findByToken(String token);

}
