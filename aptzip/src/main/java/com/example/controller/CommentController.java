package com.example.controller;

import com.example.domain.board.BoardEntity;
import com.example.domain.board.CommentEntity;
import com.example.domain.user.AptzipUserEntity;
import com.example.domain.user.UserRequestDto;
import com.example.domain.user.UserResponseDto;
import com.example.persistence.CommentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/comment/")
public class CommentController {

	@Autowired
	private CommentRepository commentRepo;

	@PostMapping("/{boardId}")
	public CommentEntity commentPost(@PathVariable("boardId") Long boardId, CommentEntity comment, @AuthenticationPrincipal UserResponseDto principal) {
    log.info("/comment/post//////////////////////////////////////////////////////////");
    BoardEntity board = new BoardEntity();
    board.setId(boardId);
    
    comment.setUser(AptzipUserEntity.builder()
                                    .id(principal.getId())
                                    .username(principal.getUsername())
                                    .build());
    comment.setBoard(board);
    comment.setCommentStatus("Y");

    log.info(board.toString());
    log.info(comment.toString());
    log.info(principal.toString());

    commentRepo.save(comment);

    return comment;
  }

}