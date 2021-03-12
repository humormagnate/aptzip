package com.markruler.aptzip.domain.board;

import java.time.LocalDateTime;

import com.markruler.aptzip.domain.user.UserAccountEntity;

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
public class CommentRequestDto {
  private Long id;
  private String content;
  private String ipAddress;
  private LocalDateTime createDate;
  private LocalDateTime updateDate;
  private boolean enabled;
  private BoardEntity board;
  private UserAccountEntity user;

  public CommentRequestDto(CommentEntity comment) {
    this.id = comment.getId();
    this.content = comment.getContent();
    this.ipAddress = comment.getIpAddress();
    this.createDate = comment.getCreateDate();
    this.updateDate = comment.getUpdateDate();
    this.enabled = comment.isEnabled();
    this.board = comment.getBoard();
    this.user = comment.getUser();
  }

  public CommentEntity toEntity() {
    // @formatter:off
    return new CommentEntity(
      this.id,
      this.content,
      this.ipAddress,
      this.createDate,
      this.updateDate,
      this.enabled,
      this.board,
      this.user
    );
    // @formatter:on
  }
}
