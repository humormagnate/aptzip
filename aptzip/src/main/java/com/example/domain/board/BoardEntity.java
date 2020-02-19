package com.example.domain.board;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.example.domain.user.AptEntity;
import com.example.domain.user.AptzipUserEntity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "TB_BOARD")
@EqualsAndHashCode(of = "id")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "board_id")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private CategoryEntity category;
	// private String category;

  @Column(name = "board_title")
	private String boardTitle;
  @Column(name = "board_content")
	private String boardContent;
	
	// @ColumnDefault("Y")
	// Column 'board_status' cannot be null -> Why? default 값도 전혀 적용안된다.
	@Column(columnDefinition = "varchar(1) default 'Y'", name = "board_status")
	private String boardStatus;
	
	@Column(name = "view_count")
	@ColumnDefault(value = "0")
	private long viewCount;
	
	@CreationTimestamp
  @Column(name = "create_date")
	private LocalDateTime createDate;
	@UpdateTimestamp
  @Column(name = "update_date")
	private LocalDateTime updateDate;
	
	// mappedBy 안하면 tb_board_comments 테이블도 생김 (양방향 정규화)
	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
	@OrderBy("id asc")
	private List<CommentEntity> comments;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private AptzipUserEntity user;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "apt_id")
	private AptEntity apt;
}
