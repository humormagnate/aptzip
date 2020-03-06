package com.markruler.controller;

import com.markruler.domain.board.BoardEntity;
import com.markruler.domain.board.LikeEntity;
import com.markruler.domain.user.UserResponseDto;
import com.markruler.persistence.board.LikeRepository;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/like")
@RestController
@RequiredArgsConstructor
public class LikeController {

  private final LikeRepository likeRepo;

  // TODO post와 delete 나누기
  @PostMapping("/{id}")
  public String insertLike(@PathVariable("id") String id, @RequestBody LikeEntity like, @AuthenticationPrincipal UserResponseDto principal) {
    log.info("like>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    log.info("id : {}", id);
    log.info("like : {}", like);
    log.info("like principal : {}", principal);
    
    BoardEntity board = new BoardEntity();
    board.setId(Long.valueOf(id));

    LikeEntity existLike = likeRepo.findByBoardAndUser(board, principal.toEntity()).orElse(null);
    log.info("exist like : {}", existLike);

    if (existLike != null) {
      likeRepo.deleteById(existLike.getId());
      return "delete";
    } else {
      like.setBoard(board);
      like.setUser(principal.toEntity());
      likeRepo.save(like);
      return "insert";
    }

  }

}