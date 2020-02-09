package com.example.config.security;

import static com.example.config.security.ApplicationUserRole.ADMIN;
import static com.example.config.security.ApplicationUserRole.USER;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.config.security.auth.ApplicationUserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

  private final PasswordEncoder passwordEncoder;
  private final ApplicationUserService applicationUserService;

  @Autowired
	public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService) {
  	this.passwordEncoder = passwordEncoder;
  	this.applicationUserService = applicationUserService;
  }

//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers("/node_modules/**", "/js/**", "/img/**", "/lib/**", "/h2-console/**");
//	}
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf().disable();
      	// .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
      	// .ignoringAntMatchers("/h2-console/**")
    http
        .authorizeRequests()
        .antMatchers("/", "index", "/css/**", "/js/**", "/images/**", "/lib/**", "/h2-console/**").permitAll()
        .antMatchers("/admin/**").hasRole(ADMIN.name())
        .anyRequest()
        .authenticated()
      // .and()
      //  .exceptionHandling().accessDeniedPage("/denied")
      .and()
      	.headers().frameOptions().disable()
      .and()
      	.httpBasic()
			.and()
				.formLogin()
				.loginPage("/user/go/login")
	      .loginProcessingUrl("/user/login")
				.defaultSuccessUrl("/", true)
				.usernameParameter("userName")
				.passwordParameter("password")
				.permitAll()
      .and()
	      .rememberMe()
	      .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
	      .key("somethingverysecured")
	      .rememberMeParameter("remember-me")
      .and()
      	.logout()
        .logoutUrl("/user/logout")
        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout", "GET")) // https://docs.spring.io/spring-security/site/docs/4.2.12.RELEASE/apidocs/org/springframework/security/config/annotation/web/configurers/LogoutConfigurer.html
        .clearAuthentication(true)
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID", "remember-me")
        .logoutSuccessUrl("/")
      ;
  }

//	@Override
//	@Bean
//	protected UserDetailsService userDetailsService() {
//		UserDetails user = User.builder().username("user").password(passwordEncoder.encode("user"))
//				.authorities(USER.getGrantedAuthorities()).build();
//
//		UserDetails admin = User.builder().username("admin").password(passwordEncoder.encode("admin"))
//				.authorities(ADMIN.getGrantedAuthorities()).build();
//
//		return new InMemoryUserDetailsManager(user, admin);
//
//	}

  @Autowired
  protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(daoAuthenticationProvider());
  }
  
 
  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder);
    provider.setUserDetailsService(applicationUserService);
    return provider;
  }
}
