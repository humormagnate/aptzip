package com.markruler.aptzip.persistence.board;

import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.helper.CustomPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
  Page<BoardEntity> findBoardByDynamicQuery(Pageable pageable, CustomPage CustomPage);
}