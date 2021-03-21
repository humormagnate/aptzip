package com.markruler.aptzip.domain.board.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.markruler.aptzip.domain.user.model.UserAccountEntity;

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
  @JoinColumn(name = "board_id", updatable = false)
  private BoardEntity board;

  @ManyToOne
  @JoinColumn(name = "user_id", updatable = false)
  private UserAccountEntity user;

  @CreationTimestamp
  private LocalDateTime createdDate;
}
