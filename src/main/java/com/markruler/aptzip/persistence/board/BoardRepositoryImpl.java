package com.markruler.aptzip.persistence.board;

import static com.markruler.aptzip.domain.board.QBoardEntity.boardEntity;

import java.util.List;

import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.vo.PageVo;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.StringUtils;

// @lombok.extern.slf4j.Slf4j
public class BoardRepositoryImpl extends QuerydslRepositorySupport implements BoardRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public BoardRepositoryImpl(JPAQueryFactory queryFactory) {
    super(BoardEntity.class);
    this.queryFactory = queryFactory;
  }

  @Override
  public Page<BoardEntity> findBoardByDynamicQuery(Pageable pageable, PageVo pageVo) {
    JPQLQuery<BoardEntity> query =
      queryFactory
        .selectFrom(boardEntity)
        .where(
          containsTitle(pageVo.getQuery()), //.or(containsContent(pageVo.getQuery())),
          containsWriter(pageVo.getUsername()),
          containsCategory(pageVo.getCategory()),
          eqApt(pageVo.getAptId())
        );

    // QuerydslRepositorySupport
    List<BoardEntity> content = getQuerydsl().applyPagination(pageable, query).fetch();
    long total = query.fetchCount();

    return new PageImpl<>(content, pageable, total);
  }

  private BooleanExpression containsTitle(String title) {
    if (StringUtils.isEmpty(title)) {
        return null;
    }
    return boardEntity.boardTitle.contains(title);
}

  private BooleanExpression containsContent(String content) {
    if (StringUtils.isEmpty(content)) {
      return null;
    }
    return boardEntity.boardContent.contains(content);
  }

  private BooleanExpression containsWriter(String writer) {
    if (StringUtils.isEmpty(writer)) {
      return null;
    }
    return boardEntity.user.username.contains(writer);
  }

  private BooleanExpression containsCategory(String category) {
    if (StringUtils.isEmpty(category)) {
      return null;
    }
    return boardEntity.category.name.contains(category);
  }

  private BooleanExpression eqApt(Long aptId) {
    if (StringUtils.isEmpty(aptId)) {
      return null;
    }
    return boardEntity.apt.id.eq(aptId);
  }

}