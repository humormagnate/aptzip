package com.markruler.aptzip.domain.board.repository;

import java.util.List;
import java.util.Optional;

import com.markruler.aptzip.domain.board.model.BoardEntity;
import com.markruler.aptzip.domain.board.model.LikeEntity;
import com.markruler.aptzip.domain.user.model.UserAccountEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends CrudRepository<LikeEntity, Long> {

  Optional<LikeEntity> findByBoardAndUser(BoardEntity board, UserAccountEntity user);

  List<LikeEntity> findAllByBoard(BoardEntity board);

}
