package com.example.persistence;

import java.util.List;

import com.example.domain.user.AptzipUserEntity;
import com.example.domain.user.UserFollowEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<UserFollowEntity, Long> {

  @Query("SELECT f FROM UserFollowEntity f INNER JOIN f.follower r WHERE r.id = :id")
  List<UserFollowEntity> findAllByFollowing(Long id);
  
  @Query("SELECT f FROM UserFollowEntity f INNER JOIN f.following r WHERE r.id = :id")
  List<UserFollowEntity> findAllByFollower(Long id);

  UserFollowEntity findByFollowingAndFollower(AptzipUserEntity following, AptzipUserEntity follower);

  // public List<UserFollowEntity> findAllByToUserId(Long id);

  // @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM UserFollowEntity f WHERE f.fromUserId = :fromId")
  // public Boolean existsByUserId(Long fromId);

  // public Boolean existsByToUserId(Long toId);

  // public Boolean existsByFromUserIdAndToUserId(Long fromId, Long toId);
}