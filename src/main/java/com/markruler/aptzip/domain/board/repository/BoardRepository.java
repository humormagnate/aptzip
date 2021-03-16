package com.markruler.aptzip.domain.board.repository;

import java.util.List;

import com.markruler.aptzip.domain.apartment.model.AptEntity;
import com.markruler.aptzip.domain.board.model.BoardEntity;
import com.markruler.aptzip.domain.board.model.Category;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository
    extends CrudRepository<BoardEntity, Long>, QuerydslPredicateExecutor<BoardEntity>, BoardRepositoryCustom {

  @Modifying
  @Query(
  // @formatter:off
    "update BoardEntity b " +
    "set b.title = :title, " +
    "b.content = :content, " +
    "b.category = :category, " +
    "b.lastModifiedDate = CURRENT_TIMESTAMP " +
    "where b.id = :id"
  )
  public void updateById(
    @Param("id") Long id,
    @Param("title") String title,
    @Param("content") String content,
    @Param("category") Category category
  // @formatter:on
  );

  public List<BoardEntity> findByUserIdOrderByIdDesc(Long userId);

  public List<BoardEntity> findAllOrderById(@Param("id") Long id);

  public List<BoardEntity> findAllByOrderByIdDesc();

  public List<BoardEntity> findAllByAptOrderByIdDesc(AptEntity apt);

  public List<BoardEntity> findAllByApt(AptEntity apt);

}
