package com.example;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import com.example.domain.board.BoardEntity;
import com.example.domain.board.CategoryEntity;
import com.example.domain.common.AptEntity;
import com.example.domain.user.AptzipUserEntity;
import com.example.persistence.board.BoardRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
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
  @Autowired
  private BoardRepository boardRepository;

  @Test
  @Transactional
  public void testList() {
    PageRequest pageRequest = PageRequest.of(0, 10, Direction.DESC, "id");
    
    Page<BoardEntity> result = boardRepository.findAll(boardRepository.makePredicate(null, null), pageRequest.first());

    log.info("PAGE : " + result.getPageable());
    log.info("////////////////////////////////////////////////////////////////////");
    result.getContent().forEach(board -> log.info("" + board));
  }

  @Test
  @Transactional
  public void testListByKeyword() {
    PageRequest pageRequest = PageRequest.of(0, 10, Direction.DESC, "id");
    
    Page<BoardEntity> result = boardRepository.findAll(boardRepository.makePredicate("title", "2"), pageRequest.first());

    log.info("PAGE : {}", result.getPageable());
    log.info("////////////////////////////////////////////////////////////////////");
    result.getContent().forEach(board -> log.info("board : {}", board));
  }

  @Test
  public void insertBoardDummies() {
    AptzipUserEntity user = AptzipUserEntity.builder().id(1).build();
    log.info("user : {}", user);
    AptEntity apt = AptEntity.builder().id(1L).build();
    log.info("apt : {}", apt);
    CategoryEntity category = new CategoryEntity(1L, "Discussion");
    log.info("category : {}", category);

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
      
      log.info("board : {}", board);
      boardRepository.save(board);
    });
  }

}