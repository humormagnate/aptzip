package com.example.persistence.board;

import java.util.List;

import com.example.domain.board.BoardEntity;
import com.example.domain.board.QBoardEntity;
import com.example.domain.common.AptEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Changsu Im
 */
public interface BoardRepository extends CrudRepository<BoardEntity, Long>,
                                          QuerydslPredicateExecutor<BoardEntity>,
                                          BoardRepositoryCustom {

  @Transactional
  @Modifying
  @Query("update BoardEntity b" +
         "   set b.boardTitle = :title," +
         "       b.boardContent = :content," +
         "       b.updateDate = CURRENT_TIMESTAMP" +
         " where b.id = :id")
  public void updateById(@Param("id") Long id,
                         @Param("title") String title,
                         @Param("content") String content);
  
  public List<BoardEntity> findByUserIdOrderByIdDesc(long userId);

  public List<BoardEntity> findAllOrderById(@Param("id") Long id);
  
  public List<BoardEntity> findAllByOrderByIdDesc();

  public List<BoardEntity> findAllByAptOrderByIdDesc(AptEntity apt);

  // Java 8 can add methods to interfaces by adding a 'default' keyword to methods.
  public default Predicate makePredicate(String type, String query) {

    BooleanBuilder builder = new BooleanBuilder();
    QBoardEntity board = QBoardEntity.boardEntity;

    // id > 0
    builder.and (board.id.gt(0));

    // type if-else
    if (type == null) {
      return builder;
    }

    switch (type) {
    case "title":
      builder.and(board.boardTitle.like("%" + query + "%"));
      break;
    case "content":
      builder.and(board.boardContent.like("%" + query + "%"));
      break;
    case "writer":
      builder.and(board.user.username.like("%" + query + "%"));
      break;
    }

    return builder;
  }

  public List<BoardEntity> findAllByApt(AptEntity apt);

}
