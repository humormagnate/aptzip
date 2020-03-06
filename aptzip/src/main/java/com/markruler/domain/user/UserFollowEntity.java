package com.markruler.domain.user;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

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

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonBackReference(value = "following")
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "following")
  private AptzipUserEntity following;
  
  @JsonBackReference(value = "follower")
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "follower")
  private AptzipUserEntity follower;

  @CreationTimestamp
  @Column(name = "create_date")
  private LocalDateTime createDate;

}