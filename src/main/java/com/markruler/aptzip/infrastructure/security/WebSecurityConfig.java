package com.markruler.aptzip.infrastructure.security;

import java.util.EventListener;

import javax.sql.DataSource;

import com.markruler.aptzip.domain.user.model.UserRole;
import com.markruler.aptzip.domain.user.service.UserAccountService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);

  private final PasswordEncoder passwordEncoder;
  private final UserAccountService userService;
  private final DataSource dataSource;

  private static final String[] AUTH_WHITELIST = {
    // @formatter:off
    "/css/**",
    "/js/**",
    "/images/**",
    "/lib/**",
    "*.ico",
    "/swagger-resources/**",
    "/v2/api-docs",
    "/swagger-ui/**",
    "/swagger-ui.html",
    "/webjars/**"
    // @formatter:on
  };

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers(AUTH_WHITELIST);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http
    // @formatter:off
      .sessionManagement()
        .maximumSessions(1)
        .maxSessionsPreventsLogin(true)
        .sessionRegistry(sessionRegistry())
        .and()
        // .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
      .authorizeRequests()
        .antMatchers("/admin/**").hasRole(UserRole.ADMIN.name())
        .antMatchers("/users/**", "/boards/*/edit/**").hasAnyRole(UserRole.USER.name(), UserRole.ADMIN.name())
        .antMatchers("/boards/new", "/zip").authenticated()
        .antMatchers("/login", "/signup").anonymous()
        .antMatchers("/**").permitAll()
        .anyRequest().authenticated().and()
        .exceptionHandling().accessDeniedPage("/")
        .and()
      .csrf()
        // .disable()
        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        .ignoringAntMatchers("/**")
        .and()
      .headers()
        .frameOptions()
          .sameOrigin()
        .xssProtection()
          .xssProtectionEnabled(true)
        .and()
        .httpStrictTransportSecurity()
          .maxAgeInSeconds(60 * 60 * 24 * 365L)
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
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", HttpMethod.GET.name()))
        .clearAuthentication(true)
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID", "remember-me")
        .logoutSuccessUrl("/");
    // @formatter:on
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
    log.debug("Security-Config-failureHandler >>");
    return new SimpleUrlAuthenticationFailureHandler("/login?error=true");
  }

  @Bean
  public AuthenticationSuccessHandler successHandler() {
    log.debug("Security-Config-successHandler >>");
    return new LoginSuccessHandler("/");
  }

  /**
   * Register HttpSessionEventPublisher
   */
  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }

  /**
   * the servlet container will notify Spring Security (through
   * HttpSessionEventPublisher) of session life cycle events
   */
  @Bean
  public static ServletListenerRegistrationBean<EventListener> httpSessionEventPublisher() {
    return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
  }

}
