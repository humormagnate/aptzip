package com.markruler.aptzip.persistence.board;

import java.util.List;
import java.util.Optional;

import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.domain.board.LikeEntity;
import com.markruler.aptzip.domain.user.UserAccountEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends CrudRepository<LikeEntity, Long> {

  Optional<LikeEntity> findByBoardAndUser(BoardEntity board, UserAccountEntity user);

  List<LikeEntity> findAllByBoard(BoardEntity board);

}
