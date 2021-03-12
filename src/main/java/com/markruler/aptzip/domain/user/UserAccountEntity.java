package com.markruler.aptzip.domain.user;

import java.io.Serializable;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.markruler.aptzip.domain.apartment.AptEntity;
import com.markruler.aptzip.domain.board.BoardEntity;

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
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = { "password", "following", "follower", "board" })
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_USER")
@Entity
public class UserAccountEntity implements Serializable {
  private static final long serialVersionUID = -8064298299029398631L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 30, nullable = false, unique = true)
  private String email;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

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

  @Column(name = "role")
  private String role;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "apt_code")
  private AptEntity apt;

  @JsonIgnore
  @OneToMany(mappedBy = "following", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<UserFollowEntity> following;

  @JsonIgnore
  @OneToMany(mappedBy = "follower", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<UserFollowEntity> follower;

  @Column(nullable = false)
  private boolean isEnabled;

}
