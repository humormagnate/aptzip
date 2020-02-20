package com.example.domain.user;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.example.domain.common.AptEntity;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity(name = "tb_user")
@EqualsAndHashCode(of = "id")
@Builder
// @Table(name = "TB_USER")
@ToString(exclude = {"following", "follower"})
@NoArgsConstructor
@AllArgsConstructor
// @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class) //=> Id 값이 원하는대로 나오지 않음
public class AptzipUserEntity {

  //@GeneratedValue(strategy = GenerationType.AUTO)
	//@GenericGenerator(name = "system-uuid", strategy = "uuid")
  @Id
  @Column(name = "user_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
	
  @Column(length = 30, nullable = false, unique = true)
  private String email;

  @Column(length = 20)
  private String phone;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, unique = true)
  private String username;

  private String address;
  
  @Column(length = 1)
  private String gender;
  
  @Lob
  private String introduction;
  
  // 하이버네이트 5.2 이상을 사용하고 있다면 Java8 날짜와 시간에 CreationTimestamp과 UpdateTimestamp을 추가해 주면 시간 설정을 하이버네이트가 알아서 해준다.
  @CreationTimestamp
  private LocalDateTime signupDate;

  @Column(nullable = false)
  private int reported;

  // cascade = CascadeType.ALL
  // -> User 객체를 insert 하는 순간 UserRole 객체도 insert 하려니까 Unique 제약조건 위배 (ConstraintViolationException)
  // @Transient
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "role")
  private AptzipRoleEntity role;
  
  // @Transient
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "apt_id")
  private AptEntity apt;
  
  // @OneToMany(mappedBy = "follow.from")
  // private List<UserFollowerEntity> following;

  // @OneToMany(mappedBy = "follow.to")
  // private List<UserFollowerEntity> follower;

  // com.example.domain.user.AptzipUserEntity$HibernateProxy$89TzX5Jf["following"]
  // ->org.hibernate.collection.internal.PersistentBag[0]
  // ->com.example.domain.user.UserFollowerEntity["from"]
  // ->com.example.domain.user.AptzipUserEntity$HibernateProxy$89TzX5Jf["following"]
  // java.lang.StackOverflowError
  // https://pasudo123.tistory.com/350
  // https://www.baeldung.com/jackson-bidirectional-relationships-and-infinite-recursion
  // Infinite recursion (StackOverflowError)
  // following.from -> me
  // following.to -> following
  @JsonIgnore
  // @JsonManagedReference(value = "following")
  @OneToMany(mappedBy = "following", fetch = FetchType.LAZY)
  private List<UserFollowEntity> following;
  
  // follwer.from -> follower
  // follwer.to -> me
  @JsonIgnore
  // @JsonManagedReference(value = "follower")
  @OneToMany(mappedBy = "follower", fetch = FetchType.LAZY)
  private List<UserFollowEntity> follower;

}