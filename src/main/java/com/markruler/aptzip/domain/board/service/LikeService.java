package com.markruler.aptzip.domain.board.service;

import java.util.List;

import com.markruler.aptzip.domain.board.model.BoardEntity;
import com.markruler.aptzip.domain.board.model.LikeEntity;
import com.markruler.aptzip.domain.board.model.LikeRequestDto;

public interface LikeService {
  LikeEntity save(LikeRequestDto like);

  List<LikeEntity> findAll();

  void delete(LikeRequestDto like);

  List<LikeEntity> findLikesByBoard(BoardEntity board);
}
