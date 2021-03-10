package com.markruler.aptzip.domain.board;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.markruler.aptzip.domain.apartment.AptEntity;
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
public class BoardRequestDto {
  private static final long serialVersionUID = 1L;

  private Long id;
  private CategoryEntity category;

  private String boardTitle;
  private String boardContent;
  private Boolean isEnabled;
  private Long viewCount;

  private LocalDateTime createDate;
  private LocalDateTime updateDate;

  private UserAccountEntity user;
  private AptEntity apt;

  @JsonIgnore
  private List<CommentEntity> comments;

  public BoardEntity toEntity() {
    // @formatter:off
    return new BoardEntity(
      this.id,
      this.category,
      this.boardTitle,
      this.boardContent,
      this.isEnabled,
      this.viewCount,
      this.createDate,
      this.updateDate,
      this.comments,
      this.user,
      this.apt
    );
    // @formatter:on
  }
}
