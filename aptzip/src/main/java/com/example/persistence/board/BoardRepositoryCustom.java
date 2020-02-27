package com.example.persistence.board;

import com.example.domain.board.BoardEntity;
import com.example.vo.PageVo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
  // List<BoardEntity> findBoardByDynamicQuery(String title, String content, String writer);
  Page<BoardEntity> findBoardByDynamicQuery(Pageable pageable, PageVo pageVo);
}