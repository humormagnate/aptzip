package com.example.persistence;

import com.example.domain.board.CommentEntity;

import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<CommentEntity, Long> {

}
