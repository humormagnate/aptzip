// package com.markruler.aptzip.persistence;

// import java.util.List;

// import com.markruler.aptzip.domain.user.AptzipUserEntity;
// import com.markruler.aptzip.domain.user.QAptzipUserEntity;
// import com.markruler.aptzip.domain.user.QUserFollowEntity;
// import com.querydsl.jpa.impl.JPAQuery;
// import com.querydsl.jpa.impl.JPAQueryFactory;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Repository;

// import lombok.extern.slf4j.Slf4j;

// @Slf4j
// @Repository
// public class FollowQueryRepository {

//   // Parameter 0 of constructor in com.markruler.persistence.FollowQueryRepository required a bean of type 'com.querydsl.jpa.impl.JPAQueryFactory' that could not be found.
//   private JPAQueryFactory jpaQueryFactory;
//   @Autowired private FollowRepository followRepo;

//   public FollowQueryRepository() {
//     super();
//   }

//   public FollowQueryRepository (JPAQueryFactory jpaQueryFactory, FollowRepository followRepo) {
//     this.jpaQueryFactory = jpaQueryFactory;
//     this.followRepo = followRepo;
//   }

//   private JPAQuery<AptzipUserEntity> followingQuery(Long fromId) {
//     QAptzipUserEntity user = QAptzipUserEntity.aptzipUserEntity;
//     QUserFollowEntity follow = QUserFollowEntity.userFollowEntity;
//     log.debug("Quser : {}", user.toString());
//     log.debug("Qfollow : {}", follow.toString());

//     JPAQuery<AptzipUserEntity> query =
//       jpaQueryFactory
//         .selectFrom(user)
//         .join(follow)
//         .on(user.id.eq(follow.fromUserId));

//     // if (fromId != null) {
//     //   query.where(follow.fromUserId.eq(fromId));
//     // }

//     return query;
//   }

//   public List<AptzipUserEntity> following(Long fromId, Long limit, Long offset) {
//     log.debug("fromId : {}", fromId);
//     log.debug("limit : {}", limit);
//     log.debug("offset : {}", offset);
    
//     JPAQuery<AptzipUserEntity> query = new JPAQuery<>();
//     Boolean test = followRepo.existsByUserId(fromId);
//     log.debug(test.toString());

//     if (followRepo.existsByUserId(fromId)) {
//       query = followingQuery(fromId);
//     }
    
//     QUserFollowEntity follow = QUserFollowEntity.userFollowEntity;
//     query.orderBy(follow.createDate.desc());

//     if (limit != null) {
//       query.limit(limit);
//     }

//     if (offset != null) {
//       query.offset(offset);
//     }

//     return query.fetch();
//   }
// }