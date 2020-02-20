package com.example.domain.user;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_user_follow")
public class UserFollowEntity {

  // Caused by: org.hibernate.AnnotationException:
  // No identifier specified for entity: com.example.domain.user.UserFollowerEntity
  // -> @Embeddable

  // @NonNull
  // @EmbeddedId
  // private FollowKey follow;

  @Id
  @Column(name = "follow_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // StackOverFlow Error -> QueryDSL?
  @JoinColumn(name = "following")
  @ManyToOne
  private AptzipUserEntity following;
  
  @JoinColumn(name = "follower")
  @ManyToOne
  private AptzipUserEntity follower;

  // @Column(name = "from_user_id")
  // private Long fromUserId;
  
  // @Column(name = "to_user_id")
  // private Long toUserId;

  @CreationTimestamp
  @Column(name = "create_date")
  private LocalDateTime createDate;

  // @PrePersist
  // public void prePersist() {
  //   if (createDate == null) createDate = LocalDateTime.now();
  // }

}