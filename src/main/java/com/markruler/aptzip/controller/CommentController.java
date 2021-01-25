package com.markruler.aptzip.controller;

import java.util.List;
import javax.transaction.Transactional;
import com.markruler.aptzip.domain.board.CommentEntity;
import com.markruler.aptzip.domain.user.UserResponseDto;
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
@RequestMapping("/comment/")
@RestController
public class CommentController {

  private final CommentService commentService;

  @GetMapping("/{boardId}")
  public ResponseEntity<List<CommentEntity>> commentGet(@PathVariable("boardId") Long boardId) {
    List<CommentEntity> list = commentService.listComments(boardId);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @Transactional
  @PostMapping("/{boardId}")
  public ResponseEntity<List<CommentEntity>> commentPost(
    // @formatter:off
    @PathVariable("boardId") Long boardId,
    // TODO: Replace this persistent entity with a simple POJO or DTO object.sonarlint(java:S4684)
    @RequestBody CommentEntity comment,
    @AuthenticationPrincipal UserResponseDto principal
  // @formatter:on
  ) {
    boolean createResult = commentService.create(boardId, comment, principal);
    if (!createResult) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    List<CommentEntity> list = commentService.listComments(boardId);
    return new ResponseEntity<>(list, HttpStatus.CREATED);
  }

  @Transactional
  @DeleteMapping("/{boardId}/{commentId}")
  public ResponseEntity<List<CommentEntity>> commentDelete(
  // @formatter:off
    @PathVariable("boardId") Long boardId,
    @PathVariable("commentId") Long commentId
  // @formatter:on
  ) {
    commentService.deleteById(commentId);
    List<CommentEntity> list = commentService.listComments(boardId);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @Transactional
  @PutMapping("/{boardId}")
  public ResponseEntity<List<CommentEntity>> updateComment(
  // @formatter:off
    @PathVariable("boardId") Long boardId,
    // TODO: Replace this persistent entity with a simple POJO or DTO object.sonarlint(java:S4684)
    @RequestBody CommentEntity comment
  // @formatter:on
  ) {
    commentService.updateComment(comment);
    List<CommentEntity> list = commentService.listComments(boardId);
    return new ResponseEntity<>(list, HttpStatus.CREATED);
  }

}
