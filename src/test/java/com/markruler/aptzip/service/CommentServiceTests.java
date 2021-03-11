package com.markruler.aptzip.service;

import static org.mockito.ArgumentMatchers.any;

import com.markruler.aptzip.domain.board.CommentEntity;
import com.markruler.aptzip.domain.board.CommentRequestDto;
import com.markruler.aptzip.domain.user.UserAccountRequestDto;
import com.markruler.aptzip.persistence.board.CommentRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class CommentServiceTests {

  @Autowired
  private CommentService service;

  @MockBean
  private CommentRepository repository;

  @Disabled("FIXME: java.lang.NullPointerException")
  @Test
  @DisplayName("사용자가 댓글을 작성합니다")
  void testSave() {
    CommentRequestDto comment = CommentRequestDto.builder().id(1L).board(null).user(null).build();
    Mockito.doReturn(comment.toEntity()).when(repository).save(any());

    CommentEntity returnedComment = service.save(1L, comment, UserAccountRequestDto.builder().id(1L).build()); // java.lang.NullPointerException
    log.debug("returned Comment: {}", returnedComment);

    Assertions.assertNotNull(returnedComment, "The saved like should not be null");
    Assertions.assertEquals(1L, returnedComment.getId());
  }
}
