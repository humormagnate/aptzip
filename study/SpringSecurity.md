# Spring Security 알게 된 것들

> WebSecurityConfigurerAdapter를 상속한 SecurityConfig

0. The `SecurityContext` and `SecurityContextHolder` are two fundamental classes of Spring Security.
1. configure(HttpSecurity) 메서드를 재정의(Override)한다.<br>
2. configure 메서드는 WebSecurity를 매개변수로 받는 Overload 함수도 있다.
3. [remember-me](https://docs.spring.io/spring-security/site/docs/3.2.0.CI-SNAPSHOT/reference/html/remember-me.html)
  - TokenBasedRememberMeServices vs. PersistentTokenBasedRememberMeServices
  - Token base 같은 경우는 다른 브라우저 혹은 다른 세션으로 접속 시 Optional 객체에서 get할 경우 java.util.NoSuchElementException: No value present 런타임 에러가 난다. 왜지? 그럼 겱구은 repository에서 객체를 가져오지 못한다는 것이고 Token이 쿠키에 없다는 뜻이다. 실제로 쿠키는 없었고..고정된 URI가 있어야 하는 건가? 아니면 서버가 계속 구동중이어야하나? -> persistentToken으로 변경하자
  - Cookie vs. DB
```java
public PersistentTokenBasedRememberMeServices(
    String key,
    UserDetailsService userDetailsService,
    PersistentTokenRepository tokenRepository) {
  super(key, userDetailsService);
  random = new SecureRandom();
  this.tokenRepository = tokenRepository;
}
```
4. oauth2login vs. formlogin
5. UserDetails와 User, UserDetailsService
6. Principal은 java.security 패키지에 있음
```java
Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
if (principal instanceof UserDetails) {
  String username = ((UserDetails)principal).getUsername();
} else {
  String username = principal.toString();
}
```
7. GrantedAuthority
8. 원하는 PasswordEncoder 객체를 반환하는 메서드를 만들고 Bean으로 만들어준다.
9. root context(/resource) 에 data.sql 이라는 파일명으로 더미데이터 만들면 load-init 될 때 자동으로 데이터를 넣어준다.

```java
@Bean
public PasswordEncoder passwordEncoder() {
  return new BCryptPasswordEncoder(10);
}
```

> UserDetailsService 인터페이스를 구현
- SecurityConfig 클래스에서 AuthenticationManagerBuilder로 DaoAuthenticationProvider 클래스를 지정해준다.
```java
@Bean
public DaoAuthenticationProvider daoAuthenticationProvider() {
  DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
  provider.setPasswordEncoder(passwordEncoder);
  provider.setUserDetailsService(userService);
  return provider;
}

@Autowired
protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
  auth.authenticationProvider(daoAuthenticationProvider());
}
```

> UserDetailsService를 상속한 UserDetailsManager 인터페이스를 구현
> <br> 하지만 UserDetailsManager는 인터페이스라서 UserDetails 매개변수 강제
> <br> JdbcUserDetailsManager 를 상속해서 변경할 수 있다.
```java
public interface UserDetailsManager extends UserDetailsService {

	void createUser(UserDetails user);

	void updateUser(UserDetails user);

	void deleteUser(String username);

	void changePassword(String oldPassword, String newPassword);

	boolean userExists(String username);

}
```

- 사용자 인증 시 DaoAuthenticationProvider 객체의 retrieveUser() 메서드에서 loadUserbyUsername() 실행

> UserDetails 인터페이스를 구현한 User를 상속
- Spring Data JPA와 같이 사용할 경우 Entity 객체와 UserDetails를 상속한 객체를 분리하자. 유지보수와 확장성.

# To-Do
- CSRF?
- Spring Security FilterChain 공부하기 (14개, loggin level = debug로 하면 볼 수 있음)
```java
1 of 14 in additional filter chain; firing Filter: 'WebAsyncManagerIntegrationFilter'
2 of 14 in additional filter chain; firing Filter: 'SecurityContextPersistenceFilter'
3 of 14 in additional filter chain; firing Filter: 'HeaderWriterFilter'
4 of 14 in additional filter chain; firing Filter: 'LogoutFilter'
5 of 14 in additional filter chain; firing Filter: 'UsernamePasswordAuthenticationFilter'
6 of 14 in additional filter chain; firing Filter: 'ConcurrentSessionFilter'
7 of 14 in additional filter chain; firing Filter: 'BasicAuthenticationFilter'
8 of 14 in additional filter chain; firing Filter: 'RequestCacheAwareFilter'
9 of 14 in additional filter chain; firing Filter: 'SecurityContextHolderAwareRequestFilter'
10 of 14 in additional filter chain; firing Filter: 'RememberMeAuthenticationFilter'
11 of 14 in additional filter chain; firing Filter: 'AnonymousAuthenticationFilter'
12 of 14 in additional filter chain; firing Filter: 'SessionManagementFilter'
13 of 14 in additional filter chain; firing Filter: 'ExceptionTranslationFilter'
14 of 14 in additional filter chain; firing Filter: 'FilterSecurityInterceptor'
```
- 가끔 InternalAuthenticationServiceException? 존재 하지 않는 아이디일 경우
- Optional\<T>, Principal, Stream과 같은 자바의 기초를 다질 것
  - JPA에서 Optional\<T> 객체를 반환
  - Context에서 인증된 사용자를 Principal 객체로 가지고 있음
  - stream, map, filter 등 함수형 프로그래밍으로 코드를 짧고 직관적으로 만들 수 있음
- 깊게 공부해보고 싶은 부분
```java
SecurityContextHolderAwareRequestFilter
UserCache
AuthenticationManagerBuilder
UsernamePasswordAuthenticationToken
InitializeUserDetailsBeanManagerConfigurer
AbstractUserDetailsAuthenticationProvider
```

# Spring
> @Resource, @Inject, @Autowired 의 차이?
- Resource : Java의 어노테이션
- Inject : Java의 어노테이션
- Autowired : Spring의 어노테이션 (강제로 연결하려면 @Qualifier)