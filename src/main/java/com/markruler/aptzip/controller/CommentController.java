package com.markruler.aptzip.controller;

import java.util.List;

import javax.transaction.Transactional;

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
  public ResponseEntity<List<CommentEntity>> commentGet(@PathVariable("boardId") Long boardId) {
    // TODO: make a response DTO
    List<CommentEntity> list = commentService.listComments(boardId);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @Transactional
  @PostMapping("/{boardId}")
  public ResponseEntity<List<CommentEntity>> commentPost(@PathVariable("boardId") Long boardId,
      @RequestBody CommentRequestDto comment, @AuthenticationPrincipal UserAccountRequestDto user) {
    log.debug("comment: {}", comment);
    CommentEntity entity = commentService.save(boardId, comment, user);
    if (entity == null) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    List<CommentEntity> list = commentService.listComments(boardId);
    return ResponseEntity.status(HttpStatus.CREATED).body(list);
  }

  @Transactional
  @DeleteMapping("/{boardId}/{commentId}")
  public ResponseEntity<List<CommentEntity>> commentDelete(@PathVariable("boardId") Long boardId,
      @PathVariable("commentId") Long commentId) {
    commentService.deleteById(commentId);
    List<CommentEntity> list = commentService.listComments(boardId);
    return ResponseEntity.ok(list);
  }

  @Transactional
  @PutMapping("/{boardId}")
  public ResponseEntity<List<CommentEntity>> updateComment(@PathVariable("boardId") Long boardId,
      @RequestBody CommentRequestDto comment) {
    commentService.updateComment(comment);
    List<CommentEntity> list = commentService.listComments(boardId);
    return ResponseEntity.status(HttpStatus.CREATED).body(list);
  }

}
