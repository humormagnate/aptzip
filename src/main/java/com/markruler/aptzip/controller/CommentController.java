package com.markruler.aptzip.controller;

import java.util.List;

import com.markruler.aptzip.domain.board.CommentEntity;
import com.markruler.aptzip.domain.board.CommentRequestDto;
import com.markruler.aptzip.domain.user.UserAccountRequestDto;
import com.markruler.aptzip.service.CommentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/comments")
@Api(tags = "comments")
@RequiredArgsConstructor
public class CommentController {
  Logger log = LoggerFactory.getLogger(CommentController.class);

  private final CommentService commentService;

  @PostMapping("/{boardId}")
  public ResponseEntity<List<CommentRequestDto>> save(@PathVariable("boardId") Long boardId,
      @RequestBody CommentRequestDto comment, @AuthenticationPrincipal UserAccountRequestDto user) {
    CommentEntity entity = commentService.save(boardId, comment, user);
    if (entity == null) {
      throw new IllegalArgumentException("no comment exists");
    }
    return ResponseEntity.ok(commentService.findAllByBoardId(boardId));
  }

  @GetMapping("/{boardId}")
  public ResponseEntity<List<CommentRequestDto>> listByBoard(@PathVariable("boardId") Long boardId) {
    return ResponseEntity.ok(commentService.findAllByBoardId(boardId));
  }

  @PutMapping("/{boardId}")
  public ResponseEntity<List<CommentRequestDto>> update(@PathVariable("boardId") Long boardId,
      @RequestBody CommentRequestDto comment, @AuthenticationPrincipal UserAccountRequestDto user) {
    if (commentService.update(comment, user) == null) {
      throw new IllegalArgumentException("update comment error");
    }
    return ResponseEntity.ok(commentService.findAllByBoardId(boardId));
  }

  @DeleteMapping("/{boardId}/{commentId}")
  public ResponseEntity<List<CommentRequestDto>> delete(@PathVariable("boardId") Long boardId,
      @PathVariable("commentId") Long commentId, @AuthenticationPrincipal UserAccountRequestDto user) {
    boolean isDeleted = commentService.deleteById(commentId, user);
    if (!isDeleted) {
      throw new IllegalArgumentException("delete comment error");
    }
    return ResponseEntity.ok(commentService.findAllByBoardId(boardId));
  }

}
