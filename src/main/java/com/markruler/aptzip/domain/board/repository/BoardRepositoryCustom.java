package com.markruler.aptzip.domain.board.repository;

import com.markruler.aptzip.domain.board.model.BoardEntity;
import com.markruler.aptzip.domain.board.model.CustomPage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
  Page<BoardEntity> findBoardByDynamicQuery(Pageable pageable, CustomPage customPage);
}