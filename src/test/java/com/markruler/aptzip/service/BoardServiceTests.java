package com.markruler.aptzip.service;

import static org.mockito.ArgumentMatchers.any;

import com.markruler.aptzip.domain.apartment.AptEntity;
import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.domain.board.CategoryEntity;
import com.markruler.aptzip.domain.user.UserAccountEntity;
import com.markruler.aptzip.persistence.board.BoardRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import lombok.extern.slf4j.Slf4j;

// TODO: 테스트 코드 작성
@SpringBootTest
@Slf4j
class BoardServiceTests {

  @Autowired
  private BoardService boardService;

  @MockBean
  private BoardRepository boardRepository;

  @Test
  @DisplayName("Test save board")
  void testSaveBoard() {
    UserAccountEntity user = UserAccountEntity.builder().id(1).build();
    AptEntity apt = AptEntity.builder().code("").build();
    CategoryEntity category = new CategoryEntity(1L, "Discussion");
    BoardEntity board = new BoardEntity();
    board.setApt(apt);
    board.setUser(user);
    board.setCategory(category);
    log.debug("board mock: {}", board);

    Mockito.doReturn(board).when(boardRepository).save(any());

    // BoardEntity returnedBoard = boardService.save(boardDTO, categoryId,
    // principal);

    Assertions.assertTrue(true);
    // Assertions.assertNotNull(returnedBoard, "The saved board should not be
    // null");
    // Assertions.assertEquals(1L, returnedBoard.getId(), "The id should be
    // incremented");
  }

}
