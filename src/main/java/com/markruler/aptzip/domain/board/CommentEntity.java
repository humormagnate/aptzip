package com.markruler.aptzip.domain.board;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.markruler.aptzip.domain.user.UserAccountEntity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_COMMENT")
public class CommentEntity {

  @Id
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

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_id")
  private BoardEntity board;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private UserAccountEntity user;
}