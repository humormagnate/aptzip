package com.example.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "tb_board")
@EqualsAndHashCode(of = "bid")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long bid;
	private String category; 
	private String user_id;
	private String apt_id;
	private String title;
	private String content;
	
	@Column(nullable=false)
	@ColumnDefault("Y")
	private String del_yn;
	private String createdate;
	private String updatedate;
}
