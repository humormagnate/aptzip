package com.markruler.aptzip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@EnableAutoConfiguration
@SpringBootApplication
public class AptzipApplication extends SpringBootServletInitializer {
  public static void main(String[] args) {
    SpringApplication.run(AptzipApplication.class, args);
  }

  @Bean
  public LocaleResolver localeResolver() {
    SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
    // sessionLocaleResolver.setDefaultLocale(java.util.Locale.ENGLISH);
    sessionLocaleResolver.setDefaultLocale(java.util.Locale.KOREAN);
    return sessionLocaleResolver;
  }

}
