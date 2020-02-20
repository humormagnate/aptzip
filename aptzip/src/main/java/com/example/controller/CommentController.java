package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.example.domain.board.BoardEntity;
import com.example.domain.board.CommentEntity;
import com.example.domain.user.UserResponseDto;
import com.example.persistence.CommentRepository;

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

  private final CommentRepository commentRepo;

  public List<CommentEntity> getCommentList(BoardEntity board) throws RuntimeException {
    log.info("/getCommentList//////////////////////////////////////////////////////////");
    // list.forEach(consumer -> consumer.getCommentContent().replace(System.lineSeparator(), "<br>"));
    return commentRepo.getCommentsByBoardId(board);
  }

  // @Secured({ "ROLE_ADMIN" }) -> ajax 가 안되는 건지, RestController가 안되는 건지.
  @GetMapping("/{boardId}")
  public ResponseEntity<List<CommentEntity>> commentGet(@PathVariable("boardId") Long boardId) {

    log.info("/comment/get//////////////////////////////////////////////////////");

    BoardEntity board = new BoardEntity();
    board.setId(boardId);

    return new ResponseEntity<>(getCommentList(board), HttpStatus.OK);
  }

  // @RequestBody error
  // nested exception is com.fasterxml.jackson.core.JsonParseException:
  // Unrecognized token 'commentContent': was expecting (JSON String, Number,
  // Array, Object or token 'null', 'true' or 'false')
  // -> 'json' 타입으로 보내줘야 함
  @Transactional
  @PostMapping("/{boardId}")
  public ResponseEntity<List<CommentEntity>> commentPost(@PathVariable("boardId") Long boardId,
      @RequestBody CommentEntity comment, @AuthenticationPrincipal UserResponseDto principal) {
    log.info("/comment/post//////////////////////////////////////////////////////////");
    log.info(comment.toString());
    BoardEntity board = new BoardEntity();
    board.setId(boardId);

    comment.setUser(principal.toEntity());
    comment.setBoard(board);
    comment.setCommentStatus("Y");
    commentRepo.save(comment);

    // log.info(board.toString());
    // log.info(comment.toString());
    // log.info(principal.toString());

    return new ResponseEntity<>(getCommentList(board), HttpStatus.CREATED);
  }

  @Transactional
  @DeleteMapping("/{boardId}/{commentId}")
  public ResponseEntity<List<CommentEntity>> commentDelete(@PathVariable("boardId") Long boardId,
      @PathVariable("commentId") Long commentId) {

    log.info("/comment/delete//////////////////////////////////////////////////////");
    commentRepo.deleteById(commentId);

    BoardEntity board = new BoardEntity();
    board.setId(boardId);

    return new ResponseEntity<>(getCommentList(board), HttpStatus.OK);
  }

  @Transactional
  @PutMapping("/{boardId}")
  public ResponseEntity<List<CommentEntity>> commentPut(@PathVariable("boardId") Long boardId,
      @RequestBody CommentEntity comment) {

    log.info("/comment/put//////////////////////////////////////////////////////");
    log.info(boardId.toString());
    log.info(comment.toString());
    commentRepo.findById(comment.getId()).ifPresent(origin -> {
      origin.setCommentContent(comment.getCommentContent());
      commentRepo.save(origin);
      log.info(origin.toString());
    });

    BoardEntity board = new BoardEntity();
    board.setId(boardId);

    return new ResponseEntity<>(getCommentList(board), HttpStatus.CREATED);
  }

}