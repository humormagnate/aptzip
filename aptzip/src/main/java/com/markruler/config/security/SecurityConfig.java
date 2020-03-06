package com.markruler.config.security;

import java.util.EventListener;

import javax.sql.DataSource;

import com.markruler.domain.user.UserRole;
import com.markruler.service.SocialUserAccountService;
import com.markruler.service.UserAccountService;

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
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final PasswordEncoder passwordEncoder;
  private final UserAccountService userService;
  private final DataSource dataSource;
    
  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/css/**", "/js/**", "/images/**", "/lib/**", "/favicon/**");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http
      .sessionManagement()
        .maximumSessions(1)
        .maxSessionsPreventsLogin(true)
        .sessionRegistry(sessionRegistry())
        .and()
        // .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
      .authorizeRequests()
        .antMatchers("/admin/**").hasRole(UserRole.ADMIN.name())
        .antMatchers("/user/**", "/board/*/edit/**").hasAnyRole(UserRole.USER.name(), UserRole.ADMIN.name())
        .antMatchers("/board/write/**", "/categories", "/zip", "/like/**").authenticated()
        .antMatchers("/login/*").anonymous()
        .antMatchers("/**").permitAll()
        .anyRequest().authenticated().and()
        .exceptionHandling().accessDeniedPage("/") // -> root path("/")를 주소창에 입력해서 접근 시 exception "Access is denied"
        .and()
      .csrf()
        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        .ignoringAntMatchers("/**")
        .and()
      .headers()
        .frameOptions()
          .sameOrigin()
        .xssProtection()
          .xssProtectionEnabled(true)
        .and()
        .httpStrictTransportSecurity() // HSTS : HTTPS 를 클라이언트 측에서 강제하는 것
          .maxAgeInSeconds(60 * 60 * 24 * 365)
          .includeSubDomains(true).and()
        .and()
      .rememberMe()
        .rememberMeServices(persistentTokenBasedRememberMeServices())
        .and()
      .formLogin()
        .loginPage("/login")
        // loginProcessingUrl -> UsernamePasswordAuthenticationFilter
        .loginProcessingUrl("/login")
        .failureUrl("/login?error=true")
        .failureHandler(failureHandler())
        .successHandler(successHandler())
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
    return rememberMeServices;
  }

  @Autowired
  protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(daoAuthenticationProvider());

    // TEST
    // auth.inMemoryAuthentication()
    // .withUser("test")
    // .password("test")
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