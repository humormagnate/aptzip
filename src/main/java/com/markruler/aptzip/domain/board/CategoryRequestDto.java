package com.markruler.aptzip.domain.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDto {

  private Long id;
  private String name;

  public CategoryEntity toEntity() {
    return new CategoryEntity(this.id, this.name);
  }
}
