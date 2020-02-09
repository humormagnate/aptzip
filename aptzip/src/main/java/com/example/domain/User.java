package com.example.domain;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;

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
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
  private int userId;
	
  @Column(length = 30, nullable = false, unique = true)
  private String email;
  
  @Column
  private String phone;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String userName;

  @Column
	private String address;
  @Column
  private String gender;
  
  @Column
  @Lob
  private String introduction;
  
  @Column(nullable = false)
  @CreatedDate
  private Timestamp signUpDate;

  @Column(nullable = false)
  private int reported;

  // @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
  // @JoinColumn(name = "user")
  @Column
  private String role;
	
  // @OneToMany(fetch = FetchType.LAZY)
  // private Apt apt;
  
	public User(int userId, String password, List<GrantedAuthority> makeGrantedAuthority) {
    this.userId = userId;
    this.password = password;
    //this.roles = makeGrantedAuthority;
  }
  
}
