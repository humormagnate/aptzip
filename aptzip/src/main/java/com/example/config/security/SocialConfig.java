package com.example.config.security;

import javax.sql.DataSource;

import com.example.persistence.user.NewJdbcUsersConnectionRepository;
import com.example.persistence.user.UserJpaRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.connect.web.SessionUserIdSource;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.security.SpringSocialConfigurer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
// @Configuration
// @EnableSocial // @Import(SocialConfiguration.class)
public class SocialConfig extends SocialConfigurerAdapter {
  
	private final DataSource dataSource;
	private final UserJpaRepository userJpaRepository;

	public SpringSocialConfigurer configure(SpringSocialConfigurer config) {
    config.alwaysUsePostLoginUrl(true);
    config.postLoginUrl("/");
		config.signupUrl("/signup");
    return config;
	}

	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {
		connectionFactoryConfigurer.addConnectionFactory(new FacebookConnectionFactory(
			environment.getProperty("spring.social.facebook.appId"),
      environment.getProperty("spring.social.facebook.appSecret")
    ));
	}

	@Override
	public UserIdSource getUserIdSource() {
		// TO:DO Auto-generated method stub
		// return super.getUserIdSource();
    return new SessionUserIdSource();
	}

	@Bean
	public SignInAdapter signInAdapter() {
		return new SocialSignInAdapter(userJpaRepository);
	}

  // rank가 MySQL 8 version 부터 reserved -> Override 필요
	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		// JdbcUsersConnectionRepository 커스터마이징해서 ConnectionRepository 에서 rank -> `rank` 고칠 것
		NewJdbcUsersConnectionRepository repository = new NewJdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
		repository.setConnectionSignUp(new SocialImplicitSignUp(userJpaRepository));
    log.info("getUsersConnectionRepository : {}", repository);
		return repository;
	}
  
	// "/connection/facebook" post
	// ConnectController
	@Bean
	public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository) {
    log.info("============================================= ConnectController =============================================");
		return new ConnectController(connectionFactoryLocator, connectionRepository);
	}

	// @Bean
	// public ProviderSignInController connectController(ConnectionFactoryLocator connectionFactoryLocator) {
  //   log.info("============================================= ConnectController =============================================");
	// 	return new ProviderSignInController(connectionFactoryLocator, getUsersConnectionRepository(connectionFactoryLocator), signInAdapter());
	// }
	
	// "/signin/facebook" post
	// ProviderSignInController
	@Bean
	public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository usersConnectionRepository) {
    log.info("============================================= ProviderSignInUtils =============================================");
		return new ProviderSignInUtils(connectionFactoryLocator, usersConnectionRepository);
	}
}