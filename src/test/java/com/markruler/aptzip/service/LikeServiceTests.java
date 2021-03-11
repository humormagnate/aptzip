package com.markruler.aptzip.service;

import static org.mockito.ArgumentMatchers.any;

import com.markruler.aptzip.domain.board.LikeEntity;
import com.markruler.aptzip.domain.board.LikeRequestDto;
import com.markruler.aptzip.persistence.board.LikeRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class LikeServiceTests {

  @Autowired
  private LikeService service;

  @MockBean
  private LikeRepository repository;

  @Test
  @DisplayName("사용자가 특정 Board에 대해 좋아요를 기록합니다")
  void testSave() {
    LikeRequestDto like = LikeRequestDto.builder().id(1L).board(null).user(null).build();
    Mockito.doReturn(like.toEntity()).when(repository).save(any());

    LikeEntity returnedLike = service.save(like);
    log.debug("returned Like: {}", returnedLike);

    Assertions.assertNotNull(returnedLike, "The saved like should not be null");
    Assertions.assertEquals(1L, returnedLike.getId());
  }

}
