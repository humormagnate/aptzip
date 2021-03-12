package com.markruler.aptzip.service;

import java.util.List;
import java.util.stream.Collectors;

import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.domain.board.BoardRequestDto;
import com.markruler.aptzip.domain.board.CommentEntity;
import com.markruler.aptzip.domain.board.CommentRequestDto;
import com.markruler.aptzip.domain.user.UserAccountRequestDto;
import com.markruler.aptzip.persistence.board.CommentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

  private final CommentRepository commentRepository;

  public List<CommentRequestDto> listComments(Long boardId) {
    BoardEntity board = BoardRequestDto.builder().id(boardId).build().toEntity();
    List<CommentRequestDto> comments = commentRepository.getCommentsByBoardId(board).stream().map(CommentRequestDto::new).collect(Collectors.toList());

    if (comments.isEmpty()) return comments;
    // board는 저장하려는 객체를 바로 수정하기 때문에 System.lineSeparator()로도 지정해서 변경할 수 있지만,
    // comment는 DB에서 가져오기 때문에 "\n"로 지정해줘야함.
    // (리눅스 환경에서도 가능할지 문제, "\r"은 안됨: 어차피 리눅스는 "\n", 윈도가 "\r\n")
    comments.forEach(comment -> comment.setContent(comment.getContent().replace("\n", "<br>"))); // System.lineSeparator()
    return comments;
  }

  public CommentEntity save(Long boardId, CommentRequestDto comment, UserAccountRequestDto user) {
    BoardEntity board = BoardRequestDto.builder().id(boardId).build().toEntity();
    comment.setUser(user.toEntity());
    comment.setBoard(board);
    comment.setEnabled(true);
    return commentRepository.save(comment.toEntity());
  }

  public CommentEntity updateComment(CommentRequestDto comment) {
    return commentRepository.save(comment.toEntity());
  }

  public void deleteById(Long commentId) {
    commentRepository.deleteById(commentId);
  }
}
