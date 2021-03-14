package com.markruler.aptzip.domain.user.repository;

import java.util.List;

import com.markruler.aptzip.domain.user.model.UserAccountEntity;
import com.markruler.aptzip.domain.user.model.UserFollowEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<UserFollowEntity, Long> {

  @Query("SELECT f FROM UserFollowEntity f INNER JOIN f.follower r WHERE r.id = :id")
  List<UserFollowEntity> findAllByFollowing(Long id);

  @Query("SELECT f FROM UserFollowEntity f INNER JOIN f.following r WHERE r.id = :id")
  List<UserFollowEntity> findAllByFollower(Long id);

  UserFollowEntity findByFollowingAndFollower(UserAccountEntity following, UserAccountEntity follower);

}