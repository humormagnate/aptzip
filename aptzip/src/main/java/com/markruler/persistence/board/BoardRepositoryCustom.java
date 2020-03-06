package com.markruler.persistence.board;

import com.markruler.domain.board.BoardEntity;
import com.markruler.vo.PageVo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
  Page<BoardEntity> findBoardByDynamicQuery(Pageable pageable, PageVo pageVo);
}