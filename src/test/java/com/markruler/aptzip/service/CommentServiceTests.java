package com.markruler.aptzip.service;

import static org.mockito.ArgumentMatchers.any;

import com.markruler.aptzip.domain.board.CommentEntity;
import com.markruler.aptzip.domain.board.CommentRequestDto;
import com.markruler.aptzip.domain.user.UserAccountRequestDto;
import com.markruler.aptzip.domain.user.UserRole;
import com.markruler.aptzip.persistence.board.CommentRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceTests {

  @InjectMocks
  private CommentService service;

  @Mock
  private CommentRepository repository;

  @Test
  @DisplayName("사용자가 댓글을 작성하면 DB에 저장합니다")
  void testSave() {
    // given
    CommentRequestDto comment = CommentRequestDto.builder().id(1L).board(null).user(null).build();
    UserAccountRequestDto user = UserAccountRequestDto.builder().id(1L).email("cxsu@aptzip.com").username("changsu")
        .role(UserRole.USER).password("password").reported(0).enabled(true).build();

    // mocking
    BDDMockito.given(repository.save(any())).willReturn(comment.toEntity());

    // when
    CommentEntity returnedComment = service.save(1L, comment, user);

    // then
    Assertions.assertNotNull(returnedComment, "The saved like should not be null");
    Assertions.assertEquals(1L, returnedComment.getId());
  }

  // TODO: Comment DTO 작성
  // @Test
  @DisplayName("특정 게시물의 댓글 엔터티를 조회합니다")
  void testFindAllByBoard() {
    // given

    // mocking

    // when

    // then
  }
}
