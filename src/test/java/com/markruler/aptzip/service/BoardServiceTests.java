package com.markruler.aptzip.service;

import static org.mockito.ArgumentMatchers.any;

import com.markruler.aptzip.domain.apartment.AptRequestDto;
import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.domain.board.BoardRequestDto;
import com.markruler.aptzip.domain.user.UserAccountEntity;
import com.markruler.aptzip.domain.user.UserRole;
import com.markruler.aptzip.persistence.board.BoardRepository;

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
class BoardServiceTests {

  @Autowired
  private BoardService service;

  @MockBean
  private BoardRepository repository;

  @Disabled("FIXME: Failed: NullPointer -> nullable = false / @NonNull")
  @Test
  @DisplayName("사용자가 Board를 작성합니다")
  void testSave() {
    AptRequestDto apt = AptRequestDto.builder().code("A10024484").build();
    // UserAccountRequestDto user =
    // UserAccountRequestDto.builder().id(1L).email("email").username("username")
    // .role(UserRole.USER).password("password").reported(0).isEnabled(true).build();
    UserAccountEntity user = UserAccountEntity.builder().id(1L).email("email").username("username")
        .role(UserRole.USER.name()).password("password").reported(0).isEnabled(true).build();
    BoardRequestDto board = BoardRequestDto.builder().apt(apt.toEntity()).user(user).build();
    Mockito.doReturn(board.toEntity()).when(repository).save(any());

    BoardEntity returnedBoard = service.save(board, "Discussion", user);
    log.debug("returned board: {}", returnedBoard);

    Assertions.assertNotNull(returnedBoard, "The saved board should not be null");
    Assertions.assertEquals(1L, returnedBoard.getId(), "The id should be incremented");
  }

}
