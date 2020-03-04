# Spring Data JPA 알게 된 것들

> 용어 정리

0. JDBC : Java Persistence의 근간
1. ORM : Object Relational Mapping
1. JPA : Java Persistence API. 말그대로 API. JPA를 정의한 javax.persistence 패키지의 대부분은 interface, enum, Exception, Annotation 으로 이루어져 있다.
1. Hibernate : JPA라는 명세의 구현체. 즉 다른 구현체를 사용해도 무방함.
1. Spring Data JPA : 반복되는 기본적인 CRUD 쿼리를 repository 인터페이스에서 자동으로 생성. EntityManager를 구경조차 안해도 됨. Named Query 규칙. (다음엔 Spring Data JPA 없이 EntityManager를 이용한 프로젝트 해볼 것)
   - SimpleJpaRepository를 참고해서 JpaRepository 구현체를 만들기 (Query DSL 이용)
   - JpaRepository를 상속한 인터페이스에서 Named Query로만 사용할 수도 있다.
1. JPQL : 표준어도 아닌데 모든 벤더를 Dialect로 만들어버리는 표준어 (@Query)
1. Query DSL ? JPQL의 빌더 클래스 (.from().where())

> EntityManager

```java
// JPA의 핵심
public interface EntityManager {
    public void persist(Object entity);
    public <T> T merge(T entity);
    public void remove(Object entity);
    public <T> T find(Class<T> entityClass, Object primaryKey);
    // More interface methods...
}

```

> @Entity

```java
@Id
@Column(name = "user_id")
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long id;

@OneToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id")
private AptzipUserEntity user;

name 값을 지정해주지 않고 schema-vendor.sql 파일로 데이터 초기화를 하게 되면 user_id와 id 2개 컬럼이 생성됨
```

> SimpleJpaRepository

> ScriptUtils

> [주의사항](https://cheese10yun.github.io/jpa-preference/)
1. Entity에 과도한 Annotation은 피하자.
```java
@Entity
@Table(
    name = "member",
    indexes = [
        Index(columnList = "username"),
        Index(columnList = "age")
    ],
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["username", "age"])
    ]
)
data class Member(
    ...
)
```

# To-Do

- DAO와 Repository의 차이
- Caused by: org.hibernate.LazyInitializationException: could not initialize proxy [com.markruler.domain.user.AptEntity#1] - no Session
  - https://jojoldu.tistory.com/146
  - https://m.blog.naver.com/inho1213/220689443632
  - https://stackoverflow.com/questions/15568112/using-proxy-target-class-true-with-spring-beans
  - 나는 단순히 loadByUsername() 메서드에서 log.info(user.toString())에서 에러가 나길래 주석 처리했더니 해결됐다. Why?