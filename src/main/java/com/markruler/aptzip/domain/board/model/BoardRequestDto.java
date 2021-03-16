package com.markruler.aptzip.domain.board.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.markruler.aptzip.domain.apartment.model.AptEntity;
import com.markruler.aptzip.domain.comment.model.CommentEntity;
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
public class BoardRequestDto {

  private Long id;
  private Category category;

  private String title;
  private String content;
  private Boolean enabled;
  private Long viewCount;

  private LocalDateTime createdDate;
  private LocalDateTime lastModifiedDate;

  private UserAccountEntity user;
  private AptEntity apt;

  @JsonIgnore
  private List<CommentEntity> comments;

  public BoardEntity toEntity() {
    // @formatter:off
    return new BoardEntity(
      this.id,
      this.category,
      this.title,
      this.content,
      this.enabled,
      this.viewCount,
      this.createdDate,
      this.lastModifiedDate,
      this.comments,
      this.user,
      this.apt
    );
    // @formatter:on
  }

  public static BoardRequestDto of(BoardEntity entity) {
    // @formatter:off
    return BoardRequestDto.builder()
      .id(entity.getId())
      .category(entity.getCategory())
      .title(entity.getTitle())
      .content(entity.getContent())
      .enabled(entity.getEnabled())
      .viewCount(entity.getViewCount())
      .createdDate(entity.getCreatedDate())
      .lastModifiedDate(entity.getLastModifiedDate())
      .comments(entity.getComments())
      .user(entity.getUser())
      .apt(entity.getApt())
      .build();
    // @formatter:on
  }

  public void increaseViewCount() {
    this.viewCount++;
  }
}
