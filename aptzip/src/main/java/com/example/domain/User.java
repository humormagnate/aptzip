package com.example.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "TB_USER")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private int userId;
	
  @Column(nullable = false, unique = true)
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
	
  // @OneToMany(fetch = FetchType.LAZY)
	// private Apt apt;
}
