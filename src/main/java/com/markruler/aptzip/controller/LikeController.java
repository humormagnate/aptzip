package com.markruler.aptzip.controller;

import com.markruler.aptzip.domain.board.LikeEntity;
import com.markruler.aptzip.domain.board.LikeRequestDto;
import com.markruler.aptzip.domain.user.UserAccountRequestDto;
import com.markruler.aptzip.service.LikeService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@Deprecated(forRemoval = true)
@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeController {

  private final LikeService likeService;

  @PostMapping("/{boardID}")
  public LikeEntity like(@PathVariable("boardID") Long boardID, @RequestBody LikeRequestDto like,
      @AuthenticationPrincipal UserAccountRequestDto user) {
    like.setUser(user.toEntity());
    return likeService.save(like);
  }

}
