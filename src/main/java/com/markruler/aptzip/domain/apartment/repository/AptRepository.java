package com.markruler.aptzip.domain.apartment.repository;

import java.util.Optional;

import com.markruler.aptzip.domain.apartment.model.AptEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AptRepository extends CrudRepository<AptEntity, String> {

  Optional<AptEntity> findByCode(String code);

}
