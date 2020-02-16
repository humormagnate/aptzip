package com.example.persistence;

import java.util.List;

import com.example.domain.board.BoardEntity;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BoardRepository extends CrudRepository<BoardEntity, Long> {
  
  public List<BoardEntity> findByUserId(long userId);

  @Transactional
  @Modifying
  @Query("update BoardEntity b" +
         "   set b.boardTitle = :title, b.boardContent = :content" +
         " where b.id = :id")
  public void updateById(@Param("id") Long id,
                         @Param("title") String title,
                         @Param("content") String content);

}
