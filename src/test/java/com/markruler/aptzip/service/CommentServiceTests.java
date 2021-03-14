package com.markruler.aptzip.service;

import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;
import java.util.List;

import com.markruler.aptzip.domain.board.model.BoardRequestDto;
import com.markruler.aptzip.domain.comment.model.CommentEntity;
import com.markruler.aptzip.domain.comment.model.CommentRequestDto;
import com.markruler.aptzip.domain.comment.repository.CommentRepository;
import com.markruler.aptzip.domain.comment.service.CommentService;
import com.markruler.aptzip.domain.user.model.UserAccountRequestDto;
import com.markruler.aptzip.domain.user.model.UserRole;

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
    Assertions.assertNotNull(returnedComment, "The saved comment should not be null");
    Assertions.assertEquals(1L, returnedComment.getId());
  }

  @Test
  @DisplayName("특정 게시물의 댓글 엔터티를 조회합니다")
  void testFindAllByBoardIdOrderByIdAsc() {
    // given
    BoardRequestDto board = BoardRequestDto.builder().id(1L).build();
    CommentEntity comment1 = CommentRequestDto.builder().id(1L).content("test1").board(board.toEntity()).build().toEntity();
    CommentEntity comment2 = CommentRequestDto.builder().id(2L).content("test2").board(board.toEntity()).build().toEntity();

    // mocking
    BDDMockito.given(repository.findAllByBoardIdOrderByIdAsc(any())).willReturn(Arrays.asList(comment1, comment2));

    // when
    List<CommentRequestDto> returnedComments = service.findAllByBoardId(board.getId());

    // then
    Assertions.assertEquals(2, returnedComments.size(), "findAll should return 2 comments");
  }
}
