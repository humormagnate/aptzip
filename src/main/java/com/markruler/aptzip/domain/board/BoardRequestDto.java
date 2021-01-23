package com.markruler.aptzip.domain.board;

import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.markruler.aptzip.domain.common.AptEntity;
import com.markruler.aptzip.domain.user.AptzipUserEntity;
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
	private Long categoryId;
	private Long viewCount;

  private String boardTitle;
	private String boardContent;
  private String boardStatus;

	private LocalDateTime createDate;
  private LocalDateTime updateDate;

	private AptzipUserEntity user;
  private AptEntity apt;

  @JsonIgnore
	private List<CommentEntity> comments;
}
