package com.markruler.aptzip.domain.board.model;

import java.time.LocalDateTime;

import com.markruler.aptzip.domain.user.model.UserAccountEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LikeRequestDto {

  private Long id;
  private BoardEntity board;
  private UserAccountEntity user;
  private LocalDateTime createDate;

  public LikeEntity toEntity() {
    return new LikeEntity(this.id, this.board, this.user, this.createDate);
  }
}
