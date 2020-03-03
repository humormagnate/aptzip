package com.example.domain.user;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_confirmation_token")
public class ConfirmationToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="token_id")
  private long tokenid;

  @Column(name="confirmation_token")
  private String confirmationToken;

  @CreationTimestamp
  private LocalDateTime createdDate;

  // @OneToOne(targetEntity = AptzipUserEntity.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", nullable = true)
  private AptzipUserEntity user;

  public ConfirmationToken(AptzipUserEntity user) {
    this.user = user;
    confirmationToken = UUID.randomUUID().toString();
  }
}