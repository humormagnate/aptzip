package com.example.domain.board;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.domain.user.AptzipUserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// 답글, 답장 : reply
// 댓글 : comment
@Getter
@Setter
@Builder
@ToString(exclude = "board")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_COMMENT")
public class CommentEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "comment_content")
  private String commentContent;

  @Column(name = "ip_address")
  private String ipAddress;

  @Column(name = "create_date")
  @CreationTimestamp
  private LocalDateTime createDate;

  @Column(name = "update_date")
  @UpdateTimestamp
  private LocalDateTime updateDate;

  @Column(name = "comment_status")
  private String commentStatus;

  // JSON string에서 제외
  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id")
  private BoardEntity board;
  
  // com.fasterxml.jackson.databind.exc.InvalidDefinitionException:
  // No serializer found for class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor
  // and no properties discovered to create BeanSerializer
  // (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS)
  // (through reference chain: java.util.ArrayList[0]
  // ->com.example.domain.board.CommentEntity["user"]
  // ->com.example.domain.user.AptzipUserEntity$HibernateProxy$34IWdfwG["hibernateLazyInitializer"])]
  // => EAGER로 지정
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private AptzipUserEntity user;
}