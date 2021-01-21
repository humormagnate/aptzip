package com.markruler.aptzip.service;

import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.persistence.board.BoardRepository;
import com.markruler.aptzip.vo.PageVo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

  private final BoardRepository boardRepository;

  public Page<BoardEntity> findBoardByDynamicQuery(Pageable pageable, PageVo pageVo) {
    return boardRepository.findBoardByDynamicQuery(pageable, pageVo);
  }

}