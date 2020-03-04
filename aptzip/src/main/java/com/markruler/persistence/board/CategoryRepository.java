package com.markruler.persistence.board;

import java.util.Optional;

import com.markruler.domain.board.CategoryEntity;

import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Long> {

  public Optional<CategoryEntity> findByName(String categoryName);

}
