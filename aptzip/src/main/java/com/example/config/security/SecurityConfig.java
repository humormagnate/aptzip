package com.example.config.security;

import java.util.concurrent.TimeUnit;

import com.example.domain.user.UserRole;
import com.example.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserService userService;

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
      .sessionManagement()
        .maximumSessions(1)
        .maxSessionsPreventsLogin(true)
        .and().and()
      .csrf()
        .disable()
        // .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        // .ignoringAntMatchers("/admin/**")
        // .and()
      .authorizeRequests()
        .antMatchers("/admin/**").hasRole(UserRole.ADMIN.name())
        .antMatchers("/user/info").hasRole(UserRole.USER.name())
        .antMatchers("/anonymous*").anonymous()
        .antMatchers("/**").permitAll()
        .anyRequest().authenticated()
      // .and()
      //  .exceptionHandling().accessDeniedPage("/denied")
        .and()
      // .headers()
      //   .xssProtection().and()
      //   .frameOptions().disable().and()
      .httpBasic()
			  .and()
      .rememberMe()
        .key("remember-me-key")
        .rememberMeParameter("remember-me")
        .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
        // .rememberMeCookieName("remember-me-cookie")
        // .userDetailsService(userService)
        // .rememberMeServices(tokenBasedRememberMeServices())
        .and()
      // => userdetailsservice is required (https://www.boraji.com/spring-security-5-remember-me-authentication-example-with-hibernate-5)
      // https://docs.spring.io/spring-security/site/docs/3.2.0.CI-SNAPSHOT/reference/html/remember-me.html
			.formLogin()
				.loginPage("/user/go/login")
	      .loginProcessingUrl("/login")
        .failureUrl("/user/go/login?error=true")
				.usernameParameter("email")
				.passwordParameter("password")
        .successHandler(successHandler())
        // .defaultSuccessUrl("/", true)
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
  public TokenBasedRememberMeServices tokenBasedRememberMeServices() {
    TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices("remember-me-key", userService);
    rememberMeServices.setAlwaysRemember(true);
    rememberMeServices.setTokenValiditySeconds(60 * 60 * 24 * 31);
    // rememberMeServices.setCookieName(TokenBasedRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY);
    // rememberMeServices.setParameter(TokenBasedRememberMeServices.DEFAULT_PARAMETER);
    // rememberMeServices.autoLogin(request, response);
    
    return rememberMeServices;
  }

  @Autowired
  protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(daoAuthenticationProvider());
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder);
    provider.setUserDetailsService(userService);
    return provider;
  }

  @Bean
  public AuthenticationSuccessHandler successHandler() {
    log.info("===============================Security-Config-successHandler=====================================");
    return new LoginSuccessHandler("/");
  }
}
