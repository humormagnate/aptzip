package com.markruler.aptzip.domain.board;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
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
  @Column(name = "create_date")
  private LocalDateTime createDate;

  @Column(name = "update_date")
  private LocalDateTime updateDate;

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
