package com.example.domain.user;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Builder
@Table(name = "TB_USER")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AptzipUserEntity {

  //@GeneratedValue(strategy = GenerationType.AUTO)
	//@GenericGenerator(name = "system-uuid", strategy = "uuid")
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
	
  @Column(length = 30, nullable = false, unique = true)
  private String email;

  @Column(length = 20)
  private String phone;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String username;

  private String address;
  
  @Column(length = 1)
  private String gender;
  
  @Lob
  private String introduction;
  
  // 하이버네이트 5.2 이상을 사용하고 있다면 Java8 날짜와 시간에 CreationTimestamp과 UpdateTimestamp을 추가해 주면 시간 설정을 하이버네이트가 알아서 해준다.
  @CreationTimestamp
  private LocalDateTime signUpDate;

  @Column(nullable = false)
  private int reported;

  //@Enumerated(EnumType.STRING)
  //@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "ROLE")
  private AptzipRoleEntity role;
	
  // @ManyToOne(fetch = FetchType.LAZY)
  // private Apt apt;
    
}