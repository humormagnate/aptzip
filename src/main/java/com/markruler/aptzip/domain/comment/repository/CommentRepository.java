package com.markruler.aptzip.domain.comment.repository;

import java.util.List;

import com.markruler.aptzip.domain.comment.model.CommentEntity;

import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<CommentEntity, Long> {

  public List<CommentEntity> findAllByBoardIdOrderByIdAsc(Long boardId);

  public List<CommentEntity> findByUserIdOrderByIdDesc(Long userId);

}
