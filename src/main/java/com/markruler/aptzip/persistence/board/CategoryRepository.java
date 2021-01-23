package com.markruler.aptzip.persistence.board;

import java.util.Optional;
import com.markruler.aptzip.domain.board.CategoryEntity;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Long> {

  public Optional<CategoryEntity> findByName(String categoryName);

}
