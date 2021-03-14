package com.markruler.aptzip.domain.board.model;

import java.time.LocalDateTime;

import com.markruler.aptzip.domain.user.model.UserAccountEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class LikeRequestDto {

  private Long id;
  private BoardEntity board;
  private UserAccountEntity user;
  private LocalDateTime createDate;

  public LikeRequestDto(LikeEntity like) {
    this.id = like.getId();
    this.board = like.getBoard();
    this.user = like.getUser();
    this.createDate = like.getCreateDate();
  }

  public LikeEntity toEntity() {
    return new LikeEntity(this.id, this.board, this.user, this.createDate);
  }

  public static LikeRequestDto of(LikeEntity entity) {
    return LikeRequestDto.builder().id(entity.getId()).board(entity.getBoard()).user(entity.getUser())
        .createDate(entity.getCreateDate()).build();
  }
}
