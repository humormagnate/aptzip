package com.markruler.aptzip.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import com.markruler.aptzip.domain.board.CategoryEntity;
import com.markruler.aptzip.persistence.board.CategoryRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
  private final CategoryRepository categoryRepository;

  public List<CategoryEntity> findAll() {
    return StreamSupport.stream(categoryRepository.findAll().spliterator(), false)
        .collect(Collectors.toList());
  }
}
