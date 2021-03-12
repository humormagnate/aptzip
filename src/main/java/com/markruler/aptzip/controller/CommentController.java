package com.markruler.aptzip.controller;

import java.util.List;

import com.markruler.aptzip.domain.board.CommentEntity;
import com.markruler.aptzip.domain.board.CommentRequestDto;
import com.markruler.aptzip.domain.user.UserAccountRequestDto;
import com.markruler.aptzip.service.CommentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/comments")
@RestController
public class CommentController {

  private final CommentService commentService;

  @GetMapping("/{boardId}")
  public ResponseEntity<List<CommentRequestDto>> commentGet(@PathVariable("boardId") Long boardId) {
    List<CommentRequestDto> list = commentService.findAllByBoard(boardId);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/{boardId}")
  public ResponseEntity<List<CommentRequestDto>> commentPost(@PathVariable("boardId") Long boardId,
      @RequestBody CommentRequestDto comment, @AuthenticationPrincipal UserAccountRequestDto user) {
    log.debug("comment: {}", comment);
    CommentEntity entity = commentService.save(boardId, comment, user);
    if (entity == null) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    List<CommentRequestDto> list = commentService.findAllByBoard(boardId);
    return ResponseEntity.ok(list);
  }

  @DeleteMapping("/{boardId}/{commentId}")
  public ResponseEntity<List<CommentRequestDto>> commentDelete(@PathVariable("boardId") Long boardId,
      @PathVariable("commentId") Long commentId) {
    commentService.deleteById(commentId);
    List<CommentRequestDto> list = commentService.findAllByBoard(boardId);
    return ResponseEntity.ok(list);
  }

  @PutMapping("/{boardId}")
  public ResponseEntity<List<CommentRequestDto>> updateComment(@PathVariable("boardId") Long boardId,
      @RequestBody CommentRequestDto comment) {
    commentService.update(comment);
    List<CommentRequestDto> list = commentService.findAllByBoard(boardId);
    return ResponseEntity.ok(list);
  }

}
