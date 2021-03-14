package com.markruler.aptzip.domain.user.repository;

import java.util.List;

import com.markruler.aptzip.domain.user.model.QUserAccountEntity;
import com.markruler.aptzip.domain.user.model.QUserFollowEntity;
import com.markruler.aptzip.domain.user.model.UserAccountEntity;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class FollowQueryRepository {
  Logger log = LoggerFactory.getLogger(FollowQueryRepository.class);

  private final FollowRepository followRepository;
  private final JPAQueryFactory jpaQueryFactory;

  public FollowQueryRepository(JPAQueryFactory jpaQueryFactory, FollowRepository followRepository) {
    this.jpaQueryFactory = jpaQueryFactory;
    this.followRepository = followRepository;
  }

  private JPAQuery<UserAccountEntity> followingQuery(Long fromId) {
    QUserAccountEntity user = QUserAccountEntity.userAccountEntity;
    QUserFollowEntity follow = QUserFollowEntity.userFollowEntity;
    log.debug("Quser : {}", user);
    log.debug("Qfollow : {}", follow);

    JPAQuery<UserAccountEntity> query = jpaQueryFactory.selectFrom(user).join(follow)
        .on(user.id.eq(follow.follower.id));

    // if (fromId != null) {
    // query.where(follow.fromUserId.eq(fromId));
    // }

    return query;
  }

  public List<UserAccountEntity> following(Long fromId, Long limit, Long offset) {
    log.debug("fromId : {}", fromId);
    log.debug("limit : {}", limit);
    log.debug("offset : {}", offset);

    JPAQuery<UserAccountEntity> query = new JPAQuery<>();
    if (followRepository.existsById(fromId)) {
      query = followingQuery(fromId);
    }

    QUserFollowEntity follow = QUserFollowEntity.userFollowEntity;
    query.orderBy(follow.createDate.desc());

    if (limit != null) {
      query.limit(limit);
    }

    if (offset != null) {
      query.offset(offset);
    }

    return query.fetch();
  }
}
