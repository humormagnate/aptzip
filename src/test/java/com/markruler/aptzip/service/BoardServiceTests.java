package com.markruler.aptzip.service;

import static org.mockito.ArgumentMatchers.any;

import com.markruler.aptzip.domain.apartment.AptRequestDto;
import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.domain.board.BoardRequestDto;
import com.markruler.aptzip.domain.board.Category;
import com.markruler.aptzip.domain.user.UserAccountEntity;
import com.markruler.aptzip.domain.user.UserRole;
import com.markruler.aptzip.persistence.board.BoardRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BoardServiceTests {

  @InjectMocks
  private BoardService service;

  @Mock
  private BoardRepository repository;

  @Test
  @DisplayName("사용자가 게시물을 작성하면 DB에 저장합니다")
  void testSave() {
    // given
    AptRequestDto apt = AptRequestDto.builder().code("A10024484").build();
    UserAccountEntity user = UserAccountEntity.builder().id(1L).email("email").username("changsu")
        .role(UserRole.USER.name()).password("password").reported(0).isEnabled(true).build();
    BoardRequestDto board = BoardRequestDto.builder().apt(apt.toEntity()).user(user).build();

    // mocking
    BDDMockito.given(repository.save(any())).willReturn(board.toEntity());
    // Mockito.doReturn(board.toEntity()).when(repository).save(any());

    // when
    BoardEntity returnedBoard = service.save(board, Category.DISCUSSION.name(), user);

    // then
    Assertions.assertNotNull(returnedBoard, "`null` 일 수 없습니다.");
    Assertions.assertEquals("changsu", returnedBoard.getUser().getUsername(), "사용자명이 changsu가 아닙니다.");
  }

}
