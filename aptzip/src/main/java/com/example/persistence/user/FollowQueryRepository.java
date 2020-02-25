// package com.example.persistence;

// import java.util.List;

// import com.example.domain.user.AptzipUserEntity;
// import com.example.domain.user.QAptzipUserEntity;
// import com.example.domain.user.QUserFollowEntity;
// import com.querydsl.jpa.impl.JPAQuery;
// import com.querydsl.jpa.impl.JPAQueryFactory;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Repository;

// import lombok.extern.slf4j.Slf4j;

// @Slf4j
// @Repository
// public class FollowQueryRepository {

//   // Parameter 0 of constructor in com.example.persistence.FollowQueryRepository required a bean of type 'com.querydsl.jpa.impl.JPAQueryFactory' that could not be found.
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
//     log.info("Quser : {}", user.toString());
//     log.info("Qfollow : {}", follow.toString());

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
//     log.info("fromId : {}", fromId);
//     log.info("limit : {}", limit);
//     log.info("offset : {}", offset);
    
//     JPAQuery<AptzipUserEntity> query = new JPAQuery<>();
//     Boolean test = followRepo.existsByUserId(fromId);
//     log.info(test.toString());

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