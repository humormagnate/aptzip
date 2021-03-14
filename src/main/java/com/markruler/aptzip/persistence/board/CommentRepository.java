package com.markruler.aptzip.persistence.board;

import java.util.List;

import com.markruler.aptzip.domain.board.CommentEntity;

import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<CommentEntity, Long> {

  public List<CommentEntity> findAllByBoardIdOrderByIdAsc(Long boardId);

  public List<CommentEntity> findByUserIdOrderByIdDesc(Long userId);

}
