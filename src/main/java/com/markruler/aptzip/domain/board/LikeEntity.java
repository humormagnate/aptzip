package com.markruler.aptzip.domain.board;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.markruler.aptzip.domain.user.UserAccountEntity;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_LIKE")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LikeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private BoardEntity board;

  @ManyToOne
  private UserAccountEntity user;

  @CreationTimestamp
  private LocalDateTime createDate;
}
