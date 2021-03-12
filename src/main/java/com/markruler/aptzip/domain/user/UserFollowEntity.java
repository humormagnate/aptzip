package com.markruler.aptzip.domain.user;

import java.io.Serializable;
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

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_USER_FOLLOW")
@Entity
public class UserFollowEntity implements Serializable {
  private static final long serialVersionUID = 1014152119956338331L;

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