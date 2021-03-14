package com.markruler.aptzip.domain.user.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TB_CONFIRMATION_TOKEN")
@Getter
@Setter
@RequiredArgsConstructor
public class ConfirmationToken {

  @Id
  @Column(name = "token")
  private String token;

  @CreationTimestamp
  private LocalDateTime createdDate;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
  private UserAccountEntity user;

  public ConfirmationToken(UserAccountEntity user) {
    this.user = user;
    this.token = UUID.randomUUID().toString();
  }
}
