package com.markruler.aptzip.domain.board.service;

import java.util.List;

import com.markruler.aptzip.domain.board.model.BoardEntity;
import com.markruler.aptzip.domain.board.model.LikeEntity;
import com.markruler.aptzip.domain.board.model.LikeRequestDto;

public interface LikeService {
  LikeEntity save(Long boardId, Long userId);

  List<LikeRequestDto> findAll();

  void delete(Long boardId, Long userId);

  List<LikeRequestDto> findLikesByBoard(BoardEntity board);
}
