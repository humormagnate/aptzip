package com.markruler.aptzip.persistence.board;

import java.util.List;

import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.domain.board.CategoryEntity;
import com.markruler.aptzip.domain.common.AptEntity;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BoardRepository
    extends CrudRepository<BoardEntity, Long>, QuerydslPredicateExecutor<BoardEntity>, BoardRepositoryCustom {

  @Transactional
  @Modifying
  @Query(
    "update BoardEntity b " +
    "set b.boardTitle = :title, " +
    "b.boardContent = :content, " +
    "b.category = :category, " +
    "b.updateDate = CURRENT_TIMESTAMP " +
    "where b.id = :id"
  )
  public void updateById(
    @Param("id") Long id,
    @Param("title") String title,
    @Param("content") String content,
    @Param("category") CategoryEntity category
    );

  public List<BoardEntity> findByUserIdOrderByIdDesc(long userId);

  public List<BoardEntity> findAllOrderById(@Param("id") Long id);

  public List<BoardEntity> findAllByOrderByIdDesc();

  public List<BoardEntity> findAllByAptOrderByIdDesc(AptEntity apt);

  public List<BoardEntity> findAllByApt(AptEntity apt);

}
