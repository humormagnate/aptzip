package com.markruler.aptzip.domain.board.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.markruler.aptzip.domain.apartment.model.AptEntity;
import com.markruler.aptzip.domain.comment.model.CommentEntity;
import com.markruler.aptzip.domain.user.model.UserAccountEntity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "TB_BOARD")
@Getter
@EqualsAndHashCode(of = "id")
@ToString(exclude = "comments")
@NoArgsConstructor
@AllArgsConstructor
public class BoardEntity implements Serializable {
  private static final long serialVersionUID = -2530764887610272058L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private Category category;

  @JsonProperty(value = "title")
  @Column(name = "title")
  private String title;

  @JsonProperty(value = "content")
  @Column(name = "content")
  private String content;

  @Column(columnDefinition = "TINYINT(1) default 1", name = "enabled")
  private Boolean enabled;

  @Column(name = "view_count")
  @ColumnDefault(value = "0")
  private Long viewCount;

  @CreationTimestamp
  @Column(name = "created_date")
  private LocalDateTime createdDate;

  @Column(name = "last_modified_date")
  private LocalDateTime lastModifiedDate;

  @JsonIgnore
  @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
  @OrderBy("id asc")
  private List<CommentEntity> comments;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private UserAccountEntity user;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "apt_code")
  private AptEntity apt;
}
