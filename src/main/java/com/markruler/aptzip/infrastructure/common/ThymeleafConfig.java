package com.markruler.aptzip.infrastructure.common;

import com.markruler.aptzip.infrastructure.thymeleaf.dialect.ThymeleafAptzipDialect;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThymeleafConfig {
  @Bean
  public ThymeleafAptzipDialect thymeleafAptzipDialect() {
    return new ThymeleafAptzipDialect();
  }
}
