package com.markruler.aptzip.service;

import java.util.List;
import java.util.stream.Collectors;

import com.markruler.aptzip.domain.board.BoardRequestDto;
import com.markruler.aptzip.domain.board.CommentEntity;
import com.markruler.aptzip.domain.board.CommentRequestDto;
import com.markruler.aptzip.domain.user.UserAccountRequestDto;
import com.markruler.aptzip.persistence.board.CommentRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
  Logger log = LoggerFactory.getLogger(CommentService.class);

  private final CommentRepository commentRepository;

  public CommentEntity save(Long boardId, CommentRequestDto comment, UserAccountRequestDto user) {
    if (user == null || !user.isEnabled()) {
      throw new IllegalArgumentException("User not found");
    }
    comment.create(BoardRequestDto.builder().id(boardId).build().toEntity(), user.toEntity());
    log.debug("comment: {}", comment);
    return commentRepository.save(comment.toEntity());
  }

  @Transactional(readOnly = true)
  public List<CommentRequestDto> findAllByBoardId(Long boardId) {
    List<CommentRequestDto> comments = commentRepository.findAllByBoardIdOrderByIdAsc(boardId).stream()
        .map(CommentRequestDto::new).collect(Collectors.toList());

    if (comments.isEmpty())
      return comments;

    // board는 저장하려는 객체를 바로 수정하기 때문에 System.lineSeparator()로도 지정해서 변경할 수 있지만,
    // comment는 DB에서 가져오기 때문에 "\n"로 지정해줘야함.
    // (리눅스 환경에서도 가능할지 문제, "\r"은 안됨: 어차피 리눅스는 "\n", 윈도가 "\r\n")
    comments.forEach(comment -> comment.setContent(comment.getContent().replace("\n", "<br>"))); // System.lineSeparator()
    log.debug("comments : {}", comments);
    return comments;
  }

  public CommentEntity update(CommentRequestDto comment, UserAccountRequestDto user) {
    log.debug("update comment: {}", comment);
    CommentEntity entity = commentRepository.findById(comment.getId()).orElseThrow(() -> {
      throw new IllegalArgumentException("not exists comment");
    });
    if (entity.getUser().getId().equals(user.getId())) {
      entity.update(comment.getContent());
      return commentRepository.save(entity);
    }
    return null;
  }

  public boolean deleteById(Long commentId, UserAccountRequestDto user) {
    CommentEntity entity = commentRepository.findById(commentId).orElseThrow(() -> {
      throw new IllegalArgumentException("not exists comment");
    });
    if (entity.getUser().getId().equals(user.getId())) {
      commentRepository.deleteById(commentId);
      return true;
    }
    return false;
  }
}
