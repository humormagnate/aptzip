package com.markruler.aptzip.domain.comment.model;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.markruler.aptzip.domain.board.model.BoardEntity;
import com.markruler.aptzip.domain.user.model.UserAccountEntity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "TB_COMMENT")
@Getter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity implements Serializable {
  private static final long serialVersionUID = 9090255029266850247L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "content")
  private String content;

  @Column(name = "ip_address")
  private String ipAddress;

  @CreationTimestamp
  @Column(name = "create_date", updatable = false)
  private LocalDateTime createDate;

  @UpdateTimestamp
  @Column(name = "update_date")
  private LocalDateTime updateDate;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "board_id", updatable = false)
  private BoardEntity board;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", updatable = false)
  private UserAccountEntity user;

  public void update(String content) {
    this.content = content;
  }
}
