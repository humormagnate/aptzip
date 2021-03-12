package com.markruler.aptzip.domain.board;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.markruler.aptzip.domain.user.UserAccountEntity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_COMMENT")
@Entity
public class CommentEntity implements Serializable {
  private static final long serialVersionUID = 9090255029266850247L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "content")
  private String content;

  @Column(name = "ip_address")
  private String ipAddress;

  @Column(name = "create_date")
  @CreationTimestamp
  private LocalDateTime createDate;

  @Column(name = "update_date")
  @UpdateTimestamp
  private LocalDateTime updateDate;

  @Column(name = "enabled")
  private boolean enabled;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_id")
  private BoardEntity board;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private UserAccountEntity user;
}