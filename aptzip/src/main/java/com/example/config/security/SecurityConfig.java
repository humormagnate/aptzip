package com.example.config.security;

import java.util.EventListener;

import javax.sql.DataSource;

import com.example.domain.user.UserPrivilege;
import com.example.domain.user.UserRole;
import com.example.service.UserService;

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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  // private final PasswordEncoder passwordEncoder;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserService userService;

  @Autowired
  private DataSource dataSource;

  @Autowired
	public SecurityConfig(PasswordEncoder passwordEncoder, UserService userService) {
  	this.passwordEncoder = passwordEncoder;
  	this.userService = userService;
  }
  
	@Override
	public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/css/**", "/js/**", "/images/**", "/lib/**");
	}
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    // 아래 순서가 중요함.
    // 실제로 스프링 문서를 보면 permitAll로 첫번째 허가를 낸 경우 authenticated 로 제한을 걸어도 걸리지 않음.
    http
      // https://gompangs.tistory.com/entry/Spring-Boot-Spring-Security-maximumSessions-%EA%B4%80%EB%A0%A8
      .sessionManagement()
        .maximumSessions(1)
          .maxSessionsPreventsLogin(true)
          .sessionRegistry(sessionRegistry())
          .and()
        .and()
      .csrf()
        .disable()
        // The server understood the request but refuses to authorize it. 403 error
        // -> /board/write post 실행 시
        // .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        // .ignoringAntMatchers("/admin/**")
        // .and()
      .authorizeRequests()
        .antMatchers("/admin/**").hasRole(UserRole.ADMIN.name())
        .antMatchers("/user/info", "/board/write", "/board/edit/**").hasAuthority(UserPrivilege.BOARD_WRITE.getPrivileges())
        .antMatchers("/anonymous*").anonymous()
        .antMatchers("/**").permitAll()
        .anyRequest().authenticated()
      // .and()
      //  .exceptionHandling().accessDeniedPage("/denied")
        .and()
      .headers()
        .xssProtection().and()
        .frameOptions().disable()
        // .httpStrictTransportSecurity() // HSTS : HTTPS 를 클라이언트 측에서 강제하는 것
        //   .maxAgeInSeconds(60 * 60 * 24 * 365)
        //   .includeSubDomains(true)
        //   .and()
        .and()
      .httpBasic()
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
      // => userdetailsservice is required (https://www.boraji.com/spring-security-5-remember-me-authentication-example-with-hibernate-5)
			.formLogin()
				.loginPage("/user/go/login")
	      .loginProcessingUrl("/login")
        .failureUrl("/user/go/login?error=true")
        // .failureForwardUrl("/user/go/login?error=true")
        .failureHandler(failureHandler())
        .successHandler(successHandler())
        // .defaultSuccessUrl("/", true)
				.usernameParameter("email")
				.passwordParameter("password")
				.permitAll()
        .and()
      .logout()
        .logoutUrl("/user/logout")
        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout", "GET")) // https://docs.spring.io/spring-security/site/docs/4.2.12.RELEASE/apidocs/org/springframework/security/config/annotation/web/configurers/LogoutConfigurer.html
        .clearAuthentication(true)
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID", "remember-me")
        .logoutSuccessUrl("/");
  }

  @Bean
  public PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices() {
    PersistentTokenBasedRememberMeServices rememberMeServices
     = new PersistentTokenBasedRememberMeServices("remember-me-key", userService, jdbcTokenRepositoryImpl());
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
    return new SimpleUrlAuthenticationFailureHandler("/user/go/login?error=true");
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

  @Bean
  public static ServletListenerRegistrationBean<EventListener> httpSessionEventPublisher() {
    return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
  }
}