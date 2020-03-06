package com.markruler.persistence.board;

import java.util.List;

import com.markruler.domain.board.BoardEntity;
import com.markruler.domain.board.CommentEntity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<CommentEntity, Long> {

  @Query("select c "
        +"from CommentEntity c "
        +"where c.board = ?1 "
        +"and c.commentStatus = 'Y' "
        +"order by c.id asc")
  public List<CommentEntity> getCommentsByBoardId(BoardEntity board);

  public List<CommentEntity> findByUserIdOrderByIdDesc(Long id);

}
