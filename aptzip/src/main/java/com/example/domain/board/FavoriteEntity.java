package com.example.domain.board;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.domain.user.AptzipUserEntity;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "tb_favorite")
public class FavoriteEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private BoardEntity board;

  @ManyToOne
  private AptzipUserEntity user;

  @CreationTimestamp
  private LocalDateTime createDate;
}