package com.markruler.aptzip.domain.comment.model;

import java.time.LocalDateTime;

import com.markruler.aptzip.domain.board.model.BoardEntity;
import com.markruler.aptzip.domain.user.model.UserAccountEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
  private Long id;
  private String content;
  private String ipAddress;
  private LocalDateTime createdDate;
  private LocalDateTime lastModifiedDate;
  private BoardEntity board;
  private UserAccountEntity user;

  public CommentRequestDto(CommentEntity comment) {
    this.id = comment.getId();
    this.content = comment.getContent();
    this.ipAddress = comment.getIpAddress();
    this.createdDate = comment.getCreatedDate();
    this.lastModifiedDate = comment.getLastModifiedDate();
    this.board = comment.getBoard();
    this.user = comment.getUser();
  }

  public CommentEntity toEntity() {
    // @formatter:off
    return new CommentEntity(
      this.id,
      this.content,
      this.ipAddress,
      this.createdDate,
      this.lastModifiedDate,
      this.board,
      this.user
    );
    // @formatter:on
  }

  public void create(BoardEntity board, UserAccountEntity user) {
    this.board = board;
    this.user = user;
  }
}
