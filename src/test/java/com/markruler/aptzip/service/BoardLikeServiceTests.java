package com.markruler.aptzip.service;

import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;
import java.util.List;

import com.markruler.aptzip.domain.board.model.BoardEntity;
import com.markruler.aptzip.domain.board.model.BoardRequestDto;
import com.markruler.aptzip.domain.board.model.LikeEntity;
import com.markruler.aptzip.domain.board.model.LikeRequestDto;
import com.markruler.aptzip.domain.board.repository.BoardRepository;
import com.markruler.aptzip.domain.board.repository.LikeRepository;
import com.markruler.aptzip.domain.board.service.BoardLikeService;
import com.markruler.aptzip.domain.user.model.UserAccountEntity;
import com.markruler.aptzip.domain.user.model.UserAccountRequestDto;
import com.markruler.aptzip.domain.user.model.UserRole;
import com.markruler.aptzip.domain.user.repository.UserJpaRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BoardLikeServiceTests {

  @InjectMocks
  private BoardLikeService service;

  @Mock
  private LikeRepository repository;
  @Mock
  private BoardRepository boardRepository;
  @Mock
  private UserJpaRepository userRepository;

  @Test
  @DisplayName("사용자가 특정 Board에 대해 좋아요를 기록합니다")
  void testSave() {
    // given
    BoardEntity board = BoardRequestDto.builder().id(1L).build().toEntity();
    UserAccountEntity user = UserAccountRequestDto.builder().id(1L).username("changsu").password("passwd")
        .role(UserRole.USER).email("cxsu@aptzip.com").enabled(true).build().toEntity();
    LikeRequestDto like = LikeRequestDto.builder().id(1L).board(board).user(user).build();

    // mocking
    BDDMockito.given(repository.save(any())).willReturn(like.toEntity());

    // when
    LikeEntity returnedLike = service.save(board.getId(), user.getId());

    // then
    Assertions.assertNotNull(returnedLike, "The saved like should not be null");
    Assertions.assertEquals(1L, returnedLike.getId());
  }

  @Test
  @DisplayName("모든 좋아요 엔터티를 조회합니다")
  void testFindAll() {
    // given
    LikeRequestDto like1 = LikeRequestDto.builder().id(1L).board(null).user(null).build();
    LikeRequestDto like2 = LikeRequestDto.builder().id(2L).board(null).user(null).build();

    // mocking
    BDDMockito.given(repository.findAll()).willReturn(Arrays.asList(like1.toEntity(), like2.toEntity()));

    // when
    List<LikeRequestDto> returnedLikes = service.findAll();

    // then
    Assertions.assertEquals(2, returnedLikes.size(), "findAll should return 2 likes");
  }

  @Test
  @DisplayName("특정 게시물의 좋아요 엔터티를 조회합니다")
  void testFindAllByBoard() {
    // given
    BoardEntity board = BoardRequestDto.builder().id(1L).build().toEntity();
    LikeRequestDto like1 = LikeRequestDto.builder().id(1L).board(board).user(null).build();
    LikeRequestDto like2 = LikeRequestDto.builder().id(2L).board(board).user(null).build();

    // mocking
    BDDMockito.given(repository.findAllByBoard(board)).willReturn(Arrays.asList(like1.toEntity(), like2.toEntity()));

    // when
    List<LikeRequestDto> returnedLikes = service.findLikesByBoard(board);

    // then
    Assertions.assertEquals(2, returnedLikes.size(), "findAll should return 2 likes");

    Mockito.verify(repository, Mockito.times(1)).findAllByBoard(any(BoardEntity.class));
    Mockito.verify(repository, Mockito.never()).delete(any(LikeEntity.class));
  }
}
