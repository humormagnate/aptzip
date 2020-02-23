package com.example.persistence;

import com.example.domain.board.BoardEntity;
import com.example.domain.board.LikeEntity;
import com.example.domain.user.AptzipUserEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends CrudRepository<LikeEntity, Long>{

	LikeEntity findByBoardAndUser(BoardEntity board, AptzipUserEntity user);
  
}