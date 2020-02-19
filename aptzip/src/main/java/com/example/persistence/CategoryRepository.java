package com.example.persistence;

import java.util.Optional;

import com.example.domain.board.CategoryEntity;

import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Long> {

  public Optional<CategoryEntity> findByName(String categoryName);

}
