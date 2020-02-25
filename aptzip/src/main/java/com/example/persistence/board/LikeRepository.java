package com.example.persistence.board;

import java.util.List;
import java.util.Optional;

import com.example.domain.board.BoardEntity;
import com.example.domain.board.LikeEntity;
import com.example.domain.user.AptzipUserEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends CrudRepository<LikeEntity, Long>{

	Optional<LikeEntity> findByBoardAndUser(BoardEntity board, AptzipUserEntity user);

	List<LikeEntity> findAllByBoard(BoardEntity board);
  
}