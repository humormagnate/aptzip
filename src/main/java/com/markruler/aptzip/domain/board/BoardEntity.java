package com.markruler.aptzip.domain.board;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.markruler.aptzip.domain.apartment.AptEntity;
import com.markruler.aptzip.domain.user.UserAccountEntity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "TB_BOARD")
@Getter
@Setter // TODO: remove setter, and make a response dto
@EqualsAndHashCode(of = "id")
@ToString(exclude = "comments")
@NoArgsConstructor
@AllArgsConstructor
public class BoardEntity implements Serializable {
  private static final long serialVersionUID = -2530764887610272058L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "category_id")
  private CategoryEntity category;

  @JsonProperty(value = "boardTitle")
  @Column(name = "board_title")
  private String boardTitle;

  @JsonProperty(value = "boardContent")
  @Column(name = "board_content")
  private String boardContent;

  @Column(columnDefinition = "TINYINT(1) default 1", name = "is_enabled")
  private Boolean isEnabled;

  @Column(name = "view_count")
  @ColumnDefault(value = "0")
  private long viewCount;

  @CreationTimestamp
  @Column(name = "create_date")
  private LocalDateTime createDate;

  @Column(name = "update_date")
  private LocalDateTime updateDate;

  @JsonIgnore
  @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
  @OrderBy("id asc")
  private List<CommentEntity> comments;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private UserAccountEntity user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "apt_code")
  private AptEntity apt;
}
