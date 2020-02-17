package com.example;

import javax.transaction.Transactional;

import com.example.domain.board.BoardEntity;
import com.example.persistence.BoardRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.annotation.Commit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
// @Commit
public class BoardRepositoryTest {

  @Autowired
  private BoardRepository boardRepo;

  @Test
  @Transactional
  public void testList() {
    PageRequest pageRequest = PageRequest.of(0, 10, Direction.DESC, "id");
    
    Page<BoardEntity> result = boardRepo.findAll(boardRepo.makePredicate(null, null), pageRequest.first());

    log.info("PAGE : " + result.getPageable());
    log.info("////////////////////////////////////////////////////////////////////");
    result.getContent().forEach(board -> log.info("" + board));
  }

  @Test
  @Transactional
  public void testListByKeyword() {
    PageRequest pageRequest = PageRequest.of(0, 10, Direction.DESC, "id");
    
    Page<BoardEntity> result = boardRepo.findAll(boardRepo.makePredicate("t", "얼음"), pageRequest.first());

    log.info("PAGE : " + result.getPageable());
    log.info("////////////////////////////////////////////////////////////////////");
    result.getContent().forEach(board -> log.info("" + board));
  }

}