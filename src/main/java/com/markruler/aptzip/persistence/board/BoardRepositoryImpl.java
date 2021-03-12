package com.markruler.aptzip.persistence.board;

import static com.markruler.aptzip.domain.board.QBoardEntity.boardEntity;

import java.util.List;

import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.domain.board.Category;
import com.markruler.aptzip.helper.CustomPage;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.StringUtils;

public class BoardRepositoryImpl extends QuerydslRepositorySupport implements BoardRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public BoardRepositoryImpl(JPAQueryFactory queryFactory) {
    super(BoardEntity.class);
    this.queryFactory = queryFactory;
  }

  @Override
  public Page<BoardEntity> findBoardByDynamicQuery(Pageable pageable, CustomPage customPage) {
    JPQLQuery<BoardEntity> query = queryFactory.selectFrom(boardEntity).where(containsTitle(customPage.getQuery()), // .or(containsContent(CustomPage.getQuery())),
        containsWriter(customPage.getWriter()), containsCategory(customPage.getCategory()),
        eqApt(customPage.getAptCode()));

    // QuerydslRepositorySupport
    List<BoardEntity> content = getQuerydsl().applyPagination(pageable, query).fetch();
    Long total = query.fetchCount();

    return new PageImpl<>(content, pageable, total);
  }

  private BooleanExpression containsTitle(String title) {
    if (StringUtils.isEmpty(title)) {
      return null;
    }
    return boardEntity.title.contains(title);
  }

  private BooleanExpression containsWriter(String writer) {
    if (StringUtils.isEmpty(writer)) {
      return null;
    }
    return boardEntity.user.username.contains(writer);
  }

  private BooleanExpression containsCategory(Category category) {
    if (StringUtils.isEmpty(category)) {
      return null;
    }
    return boardEntity.category.eq(category);
  }

  private BooleanExpression eqApt(String aptCode) {
    if (StringUtils.isEmpty(aptCode)) {
      return null;
    }
    return boardEntity.apt.code.eq(aptCode);
  }

}
