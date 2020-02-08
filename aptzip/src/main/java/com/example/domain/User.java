package com.example.domain;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "TB_USER")
@ToString
public class User {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private String userId;
	
  @Column
	private String email;
  @Column
	private String password;
  @Column
	private String userName;
  @Column
	private String address;
  @Column
	private String gender;
  @Column
	private String introduction;
  @Column
  @CreatedDate
	private Timestamp signUpDate;
  @Column
	private int reported;
	
  // @OneToMany(fetch = FetchType.LAZY)
	// private Apt apt;
}
