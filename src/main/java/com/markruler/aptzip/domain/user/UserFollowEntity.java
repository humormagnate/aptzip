package com.markruler.aptzip.domain.user;

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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "tb_user_follow")
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserFollowEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonBackReference(value = "following")
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "following")
  private UserAccountEntity following;

  @JsonBackReference(value = "follower")
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "follower")
  private UserAccountEntity follower;

  @CreationTimestamp
  @Column(name = "create_date")
  private LocalDateTime createDate;

}