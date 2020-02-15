package com.example.domain.board;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.domain.user.AptzipUserEntity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "TB_COMMENT")
@ToString(exclude = "board")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity {

  @Id
  @Column(name = "comment_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "comment_content")
  private String commentContent;

  @Column(name = "ip_address")
  private String ipAddress;

  @Column(name = "create_date")
  @CreationTimestamp
  private LocalDateTime createDate;

  @Column(name = "update_date")
  @UpdateTimestamp
  private LocalDateTime updateDate;

  @Column(name = "comment_status")
  private String commentStatus;

  @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id")
  private BoardEntity board;
  
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private AptzipUserEntity user;
}