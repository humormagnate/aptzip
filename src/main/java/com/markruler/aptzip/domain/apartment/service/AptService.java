package com.markruler.aptzip.domain.apartment.service;

import java.util.Optional;

import com.markruler.aptzip.domain.apartment.model.AptEntity;
import com.markruler.aptzip.domain.apartment.repository.AptRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AptService {
  Logger log = LoggerFactory.getLogger(AptService.class);

  private final AptRepository aptRepository;

  public AptEntity findByCode(String code) {
    Optional<AptEntity> entity = aptRepository.findByCode(code);
    if (entity.isEmpty()) {
      return null;
    }
    return entity.get();
  }
}
