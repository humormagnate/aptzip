package com.example.service;

import com.example.domain.board.BoardEntity;
import com.example.persistence.board.BoardRepository;
import com.example.vo.PageVo;

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