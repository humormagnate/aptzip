package com.markruler.persistence.board;

import java.util.List;
import java.util.Optional;

import com.markruler.domain.board.BoardEntity;
import com.markruler.domain.board.LikeEntity;
import com.markruler.domain.user.AptzipUserEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends CrudRepository<LikeEntity, Long>{

	Optional<LikeEntity> findByBoardAndUser(BoardEntity board, AptzipUserEntity user);

	List<LikeEntity> findAllByBoard(BoardEntity board);
  
}