package com.example.config.security;

import java.util.concurrent.TimeUnit;

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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.domain.UserRole;
import com.example.service.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final PasswordEncoder passwordEncoder;
  private final UserService userService;

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
    http
      .csrf().disable();
      	// .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        // .ignoringAntMatchers("/h2-console/**")
    
    http
      .authorizeRequests()
        .antMatchers("/**").permitAll()
        .antMatchers("/admin/**").hasRole("ADMIN")
        .antMatchers("/user/info").hasRole("USER")
        .anyRequest().authenticated()
      // .and()
      //  .exceptionHandling().accessDeniedPage("/denied")
        .and()
      .headers()
        .frameOptions().disable()
        .and()
      .httpBasic()
			  .and()
			.formLogin()
				.loginPage("/user/go/login")
	      .loginProcessingUrl("/user/login")
        .defaultSuccessUrl("/", true)
        //.failureUrl("/user/go/login")
				.usernameParameter("email")
				.passwordParameter("password")
        .successHandler(successHandler())
				.permitAll()
        .and()
	    // .rememberMe()
	    //   .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
	    //   .key("somethingverysecured")
	    //   .rememberMeParameter("remember-me")
      //   .and()
      // => userdetailsservice is required (https://www.boraji.com/spring-security-5-remember-me-authentication-example-with-hibernate-5)
      .logout()
        .logoutUrl("/user/logout")
        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout", "GET")) // https://docs.spring.io/spring-security/site/docs/4.2.12.RELEASE/apidocs/org/springframework/security/config/annotation/web/configurers/LogoutConfigurer.html
        .clearAuthentication(true)
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID", "remember-me")
        .logoutSuccessUrl("/")
      ;
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
    return new LoginSuccessHandler("/");
  }
}
