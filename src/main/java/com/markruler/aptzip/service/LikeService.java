package com.markruler.aptzip.service;

import java.util.List;

import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.domain.board.LikeEntity;
import com.markruler.aptzip.domain.board.LikeRequestDto;

public interface LikeService {
  LikeEntity save(LikeRequestDto like);

  List<LikeEntity> findAll();

  void delete(LikeRequestDto like);

  List<LikeEntity> findLikesByBoard(BoardEntity board);
}
