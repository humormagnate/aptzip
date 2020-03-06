package com.markruler.domain.user;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.markruler.domain.board.BoardEntity;
import com.markruler.domain.common.AptEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "tb_user")
@EqualsAndHashCode(of = "id")
@Builder
@ToString(exclude = {"following", "follower", "board"})
@NoArgsConstructor
@AllArgsConstructor
public class AptzipUserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
	
  @Column(length = 30, nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, unique = true)
  private String username;

  @Lob
  private String introduction;
  
  @CreationTimestamp
  private LocalDateTime signupDate;

  @ColumnDefault(value = "0")
  @Column(nullable = false)
  private int reported;

  @JsonIgnore
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<BoardEntity> board;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "role")
  private AptzipRoleEntity role;
  
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "apt_id")
  private AptEntity apt;
  
  @JsonIgnore
  @OneToMany(mappedBy = "following", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<UserFollowEntity> following;
  
  @JsonIgnore
  @OneToMany(mappedBy = "follower", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<UserFollowEntity> follower;

  private boolean isEnabled;

  private String providerId;
  
  private String providerUserId;

}