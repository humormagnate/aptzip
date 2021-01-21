package com.markruler.aptzip.controller;

import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.domain.board.LikeEntity;
import com.markruler.aptzip.domain.user.UserResponseDto;
import com.markruler.aptzip.persistence.board.LikeRepository;

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
    log.debug("like>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    log.debug("id : {}", id);
    log.debug("like : {}", like);
    log.debug("like principal : {}", principal);
    
    BoardEntity board = new BoardEntity();
    board.setId(Long.valueOf(id));

    LikeEntity existLike = likeRepo.findByBoardAndUser(board, principal.toEntity()).orElse(null);
    log.debug("exist like : {}", existLike);

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