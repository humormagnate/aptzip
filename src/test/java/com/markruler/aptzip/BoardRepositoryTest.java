package com.markruler.aptzip;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.domain.board.CategoryEntity;
import com.markruler.aptzip.domain.common.AptEntity;
import com.markruler.aptzip.domain.user.AptzipUserEntity;
import com.markruler.aptzip.persistence.board.BoardRepository;
import com.markruler.aptzip.service.BoardService;
import com.markruler.aptzip.vo.PageVo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AptzipApplication.class)
@Slf4j
@RequiredArgsConstructor
@Commit
public class BoardRepositoryTest {
  
  // final로 지정할 경우 ParameterResolver 필요 (JUnit5)
  // @Autowired 지정하지 않을 경우 NullPointerException
  // @Autowired private BoardRepositoryImpl boardRepositoryImpl;
  @Autowired private BoardRepository boardRepository;
  @Autowired private BoardService boardService;


  @Test
  public void insertBoardDummies() {
    AptzipUserEntity user = AptzipUserEntity.builder().id(1).build();
    log.debug("user : {}", user);
    AptEntity apt = AptEntity.builder().id(1L).build();
    log.debug("apt : {}", apt);
    CategoryEntity category = new CategoryEntity(1L, "Discussion");
    log.debug("category : {}", category);

    // range(startInclusive, endExclusive) -> 0~29 (30개)
    IntStream.range(0, 300).forEach(i -> {
      BoardEntity board = new BoardEntity();

      board.setBoardTitle("sample title" + i);
      board.setBoardContent("sample content" + i);
      board.setCategory(category);
      board.setUser(user);
      board.setApt(apt);
			board.setUpdateDate(LocalDateTime.now());
      board.setBoardStatus("Y");
      
      log.debug("board : {}", board);
      boardRepository.save(board);
    });
  }
  
  @Test
  public void testListByDynamicQuery() {
    // given

    // when
    // List<BoardEntity> boardEntities = boardRepository.findBoardByDynamicQuery("32", "1", "q");
    PageVo pageVo = PageVo.builder()
                          .page(1)
                          .size(10)
                          .query("1")
                          .username("q")
                          .category("Discussion")
                          .date("")
                          .build();

    Page<BoardEntity> content = boardService.findBoardByDynamicQuery(pageVo.makePageable(0, "id"), pageVo);
    log.debug("content : {}", content);
    log.debug("content.getTotalPages() : {}", content.getTotalPages());
    List<BoardEntity> list = content.getContent();

    // then
    if (list.size() > 0) {
      list.forEach(board -> {
        log.debug("boardEntities : {}", board.getBoardTitle());
      });
      log.debug("boardEntities.size() : {}", list.size());
    }
    assertTrue(list.size() > 1);
  }

  @Test
  public void testListByDynamicQueryWithNull() {
    // given

    // when
    // List<BoardEntity> boardEntities = boardRepository.findBoardByDynamicQuery("32", "1", "q");
    PageVo pageVo = new PageVo();

    Page<BoardEntity> content = boardService.findBoardByDynamicQuery(pageVo.makePageable(0, "id"), pageVo);
    log.debug("content : {}", content);
    log.debug("content.getTotalPages() : {}", content.getTotalPages());
    List<BoardEntity> list = content.getContent();

    // then
    if (list.size() > 0) {
      list.forEach(board -> {
        log.debug("boardEntities : {}", board.getBoardTitle());
      });
      log.debug("boardEntities.size() : {}", list.size());
    }
    assertTrue(list.size() > 1);
  }

}