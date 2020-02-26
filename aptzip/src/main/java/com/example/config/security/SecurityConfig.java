package com.example.config.security;

import java.util.EventListener;

import javax.sql.DataSource;

import com.example.domain.user.UserRole;
import com.example.service.SocialUserAccountService;
import com.example.service.UserAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// https://www.callicoder.com/spring-boot-security-oauth2-social-login-part-1/
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  
  // private PasswordEncoder passwordEncoder;
  // private UserService userService;
  // private DataSource dataSource;
  // public SecurityConfig(PasswordEncoder passwordEncoder, UserService userService, DataSource dataSource) {
  //   this.passwordEncoder = passwordEncoder;
  //   this.userService = userService;
  //   this.dataSource = dataSource;
  // }

  private final PasswordEncoder passwordEncoder;
  private final UserAccountService userService;
  private final DataSource dataSource;
    
  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/css/**", "/js/**", "/images/**", "/lib/**", "/favicon/**");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    // 모든 작업은 여러 종류의 Filter들과 Interceptor를 통해서 동작하기 때문에 개발자의 입장에서는
    // 적절한 처리르 담당하는 핸들러(Handler)들을 추가하는 것만으로 모든 처리가 완료됩니다.

    // 아래 순서가 중요함.
    // 실제로 스프링 문서를 보면 permitAll로 첫번째 허가를 낸 경우 authenticated 로 제한을 걸어도 걸리지 않음.
    http
      // .httpBasic().and() -> login form popup (HTTP 기본 인증(HTTP Basic Authentication)
      // https://gompangs.tistory.com/entry/Spring-Boot-Spring-Security-maximumSessions-%EA%B4%80%EB%A0%A8
      .sessionManagement()
        .maximumSessions(1)
        .maxSessionsPreventsLogin(true)
        .sessionRegistry(sessionRegistry())
        .and()
        // .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        // Cross-site request forgery, CSRF, XSRF
        // 요청을 보내는 URL에서 서버가 가진 동일한 값과 같은 값을 가지고 데이터를 전송할 때에만 신뢰하기 위한 방법
        // 스프링 시큐리티가 적용되면 POST 방식으로 보내는 모든 데이터는 CSRF 토큰 값이 필요하다.
        // 만일 CSRF 토큰을 사용하지 않으려면 application.properties에 security.enable-csrf 속성을 이용해서
        // 사용하지 않도록 설정할 수도 있습니다.
      .csrf()
        .disable()
        // The server understood the request but refuses to authorize it. 403 error
        // -> /board/write post 실행 시
        // .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        // .ignoringAntMatchers("/admin/**")
        // .and()
        // https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/builders/HttpSecurity.html#authorizeRequests--
        // Allows restricting access based upon the HttpServletRequest using
      .authorizeRequests()
        .antMatchers("/admin/**").hasRole(UserRole.ADMIN.name())
        .antMatchers("/user/**", "/board/*/edit/**").hasAnyRole(UserRole.USER.name(), UserRole.ADMIN.name())
        // production
        // .antMatchers("/user/info/*", "/board/edit/**", "/").hasAnyRole(UserRole.USER.name(), UserRole.ADMIN.name())
        .antMatchers("/board/write/**", "/categories", "/zip", "/like/**").authenticated()
        // .antMatchers("/board/write/**").hasAuthority(UserPrivilege.BOARD_WRITE.getPrivileges())
        .antMatchers("/login/*").anonymous()
        .antMatchers("/**").permitAll()
        .anyRequest().authenticated().and().exceptionHandling().accessDeniedPage("/") // -> root context("/")를 주소창에 입력해서 접근 시 exception "Access is denied"
        .and()
      .headers()
        .frameOptions().sameOrigin()
        // .frameOptions().disable()
        // .xssProtection().and()
        // .httpStrictTransportSecurity() // HSTS : HTTPS 를 클라이언트 측에서 강제하는 것
        // .maxAgeInSeconds(60 * 60 * 24 * 365)
        // .includeSubDomains(true).and()
        .and()
      .rememberMe()
        .rememberMeServices(persistentTokenBasedRememberMeServices())
        // .key("remember-me-key")
        // .rememberMeParameter("remember-me")
        // .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
        // .rememberMeCookieName("remember-me-cookie")
        // .userDetailsService(userService)
        // .tokenRepository(remembermeRepository)
        .and()
        // => userdetailsservice is required
        // (https://www.boraji.com/spring-security-5-remember-me-authentication-example-with-hibernate-5)
      .formLogin()
        // login vs. signin
        // https://miretia.tistory.com/477
        // https://ux.stackexchange.com/questions/1080/using-sign-in-vs-using-log-in?newreg=69abd36f2952491f89f6bc7500a4e379
        // https://web.archive.org/web/20130416031325/http://0xtc.com/2009/06/25/login-logout-vs-sign-in-sign-out-vs-log-in-sign-out-a-short-roundup.xhtml/
        .loginPage("/login")
        // loginProcessingUrl -> UsernamePasswordAuthenticationFilter
        .loginProcessingUrl("/login")
        .failureUrl("/login?error=true")
        // .failureForwardUrl("/login?error=true")
        .failureHandler(failureHandler())
        .successHandler(successHandler())
        // .defaultSuccessUrl("/", true)
        // .usernameParameter("email")
        .usernameParameter("username")
        .passwordParameter("password")
        .permitAll()
        .and()
      .logout()
        .logoutUrl("/logout")
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) // 로그아웃은 POST로 처리하는 것이 안전하다.
        // https://docs.spring.io/spring-security/site/docs/4.2.12.RELEASE/apidocs/org/springframework/security/config/annotation/web/configurers/LogoutConfigurer.html
        .clearAuthentication(true)
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID", "remember-me")
        .logoutSuccessUrl("/");
  }

  @Bean
  public PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices() {
    PersistentTokenBasedRememberMeServices rememberMeServices = new PersistentTokenBasedRememberMeServices(
        "remember-me-key", userService, jdbcTokenRepositoryImpl());
    rememberMeServices.setTokenValiditySeconds(60 * 60 * 24 * 31);
    rememberMeServices.setCookieName(TokenBasedRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY);
    rememberMeServices.setParameter(TokenBasedRememberMeServices.DEFAULT_PARAMETER);
    // rememberMeServices.setAlwaysRemember(true);
    // rememberMeServices.autoLogin(request, response);
    return rememberMeServices;
  }

  @Autowired
  protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(daoAuthenticationProvider());

    // auth.inMemoryAuthentication()
    // .withUser("root")
    // .password("aptzip")
    // .roles(UserRole.ADMIN.name());
  }

  @Bean
  public JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl() {
    JdbcTokenRepositoryImpl jtri = new JdbcTokenRepositoryImpl();
    jtri.setDataSource(dataSource);
    return jtri;
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder);
    provider.setUserDetailsService(userService);
    return provider;
  }

  @Bean
  public AuthenticationFailureHandler failureHandler() {
    log.info("===============================Security-Config-failureHandler=====================================");
    return new SimpleUrlAuthenticationFailureHandler("/login?error=true");
  }

  @Bean
  public AuthenticationSuccessHandler successHandler() {
    log.info("===============================Security-Config-successHandler=====================================");
    return new LoginSuccessHandler("/");
  }

  @Bean // Register HttpSessionEventPublisher
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }

  // the servlet container will notify
  // Spring Security (through HttpSessionEventPublisher) of session life cycle
  // events
  @Bean
  public static ServletListenerRegistrationBean<EventListener> httpSessionEventPublisher() {
    return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
  }

  @Bean
	public SocialUserAccountService socialUsersDetailService() {
		return new SocialUserAccountService(userService);
  }
  
}