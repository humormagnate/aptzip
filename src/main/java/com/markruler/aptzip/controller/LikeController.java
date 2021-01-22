package com.markruler.aptzip.controller;

import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.domain.board.LikeEntity;
import com.markruler.aptzip.domain.user.UserResponseDto;
import com.markruler.aptzip.service.LikeService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeController {

  private final LikeService likeService;

  // TODO post, put, delete 나누기
  @PostMapping("/{boardID}")
  public String like(
  // @formatter:off
    @PathVariable("boardID") Long boardID,
    // TODO: Replace this persistent entity with a simple POJO or DTO object.sonarlint(java:S4684)
    @RequestBody LikeEntity like,
    @AuthenticationPrincipal UserResponseDto principal
  // @formatter:on
  ) {
    return likeService.readByBoardAndUser(boardID, like, principal);
  }

}
