package com.example.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import com.example.domain.board.BoardEntity;

public interface BoardRepository extends CrudRepository<BoardEntity, Long> {
  
  public List<BoardEntity> findByUserId(long userId);
  
}
