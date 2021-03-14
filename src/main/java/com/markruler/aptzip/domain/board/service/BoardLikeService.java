package com.markruler.aptzip.domain.board.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.markruler.aptzip.domain.board.model.BoardEntity;
import com.markruler.aptzip.domain.board.model.BoardRequestDto;
import com.markruler.aptzip.domain.board.model.LikeEntity;
import com.markruler.aptzip.domain.board.model.LikeRequestDto;
import com.markruler.aptzip.domain.board.repository.LikeRepository;
import com.markruler.aptzip.domain.user.model.UserAccountRequestDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardLikeService implements LikeService {
  Logger log = LoggerFactory.getLogger(BoardLikeService.class);

  private final LikeRepository likeRepository;

  @Override
  public LikeEntity save(Long boardId, Long userId) {
    BoardRequestDto board = BoardRequestDto.builder().id(boardId).build();
    UserAccountRequestDto user = UserAccountRequestDto.builder().id(userId).build();
    LikeRequestDto like = LikeRequestDto.builder().board(board.toEntity()).user(user.toEntity()).build();
    return likeRepository.save(like.toEntity());
  }

  @Override
  public List<LikeRequestDto> findAll() {
    // @formatter:off
    return StreamSupport
      .stream(likeRepository.findAll().spliterator(), false)
      .map(LikeRequestDto::new)
      .collect(Collectors.toList());
    // @formatter:on
  }

  @Override
  @Transactional(readOnly = true)
  public List<LikeRequestDto> findLikesByBoard(BoardEntity board) {
    LikeRequestDto like = LikeRequestDto.builder().board(board).build();
    List<LikeEntity> likes = likeRepository.findAllByBoard(like.getBoard());
    return likes.stream().map(LikeRequestDto::new).collect(Collectors.toList());
  }

  @Override
  public void delete(Long boardId, Long userId) {
    BoardRequestDto board = BoardRequestDto.builder().id(boardId).build();
    UserAccountRequestDto user = UserAccountRequestDto.builder().id(userId).build();
    likeRepository.deleteByBoardAndUser(board.toEntity(), user.toEntity());
  }

}
