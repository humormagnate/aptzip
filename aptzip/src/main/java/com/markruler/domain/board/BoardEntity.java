package com.markruler.domain.board;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.markruler.domain.common.AptEntity;
import com.markruler.domain.user.AptzipUserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

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
@ToString(exclude = "comments")
@NoArgsConstructor
@AllArgsConstructor
public class BoardEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	// 조회수 update할 때도 변경됨
	// @UpdateTimestamp
  @Column(name = "update_date")
	private LocalDateTime updateDate;
	
  // JSON string에서 제외
  @JsonIgnore
	// mappedBy 안하면 tb_board_comments 테이블도 생김 (양방향 정규화)
	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
	@OrderBy("id asc")
	private List<CommentEntity> comments;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private AptzipUserEntity user;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "apt_id")
	private AptEntity apt;
}
