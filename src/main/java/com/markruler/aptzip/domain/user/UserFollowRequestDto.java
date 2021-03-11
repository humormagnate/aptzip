package com.markruler.aptzip.domain.user;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserFollowRequestDto {
  private Long id;
  private UserAccountEntity following;
  private UserAccountEntity follower;
  private LocalDateTime createDate;

  public UserFollowEntity toEntity() {
    // @formatter:off
    return new UserFollowEntity(
      this.id,
      this.following,
      this.follower,
      this.createDate
    );
    // @formatter:on
  }
}