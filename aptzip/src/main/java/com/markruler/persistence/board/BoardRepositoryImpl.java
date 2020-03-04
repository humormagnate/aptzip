package com.markruler.persistence.board;

import static com.markruler.domain.board.QBoardEntity.boardEntity;

import java.util.List;

import com.markruler.domain.board.BoardEntity;
import com.markruler.vo.PageVo;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BoardRepositoryImpl extends QuerydslRepositorySupport implements BoardRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public BoardRepositoryImpl(JPAQueryFactory queryFactory) {
    super(BoardEntity.class);
    this.queryFactory = queryFactory;
  }

  @Override
  public Page<BoardEntity> findBoardByDynamicQuery(Pageable pageable, PageVo pageVo) {
    log.info("pageable : {}", pageable);
    log.info("pageVo : {}", pageVo);

    // https://jojoldu.tistory.com/372
    // https://jojoldu.tistory.com/394
    // Dynamic Query
    JPQLQuery<BoardEntity> query =
      queryFactory
        .selectFrom(boardEntity)
        .where(
          // null 이면 or 메소드 호출할 때 nullpointer
          containsTitle(pageVo.getQuery()), //.or(containsContent(pageVo.getQuery())),
          containsWriter(pageVo.getUsername()),
          containsCategory(pageVo.getCategory()),
          eqApt(pageVo.getAptId())
        );
        // .orderBy(orderby)
        // .limit(limit)
        // .offset(offset)
    // JPQLQuery<BoardEntity> query =
    //   from(boardEntity)
    //   .where(
    //     containsTitle(pageVo.getQuery()).or(containsContent(pageVo.getQuery())),
    //     containsWriter(pageVo.getUsername())
    //   );
    
    // QuerydslRepositorySupport
    List<BoardEntity> content = getQuerydsl().applyPagination(pageable, query).fetch();
    long total = query.fetchCount();

    return new PageImpl<>(content, pageable, total);
  }

  // https://cherrypick.co.kr/querydsl-difference-like-contains/
  // Querydsl like, contains 차이

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