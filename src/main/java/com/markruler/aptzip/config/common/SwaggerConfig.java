package com.markruler.aptzip.config.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// http://springfox.github.io/springfox/docs/3.0.0
@Configuration
@EnableSwagger2
public class SwaggerConfig {
  @Bean
  public Docket swaggerAPI() {
    //@formatter:off
    return new Docket(DocumentationType.SWAGGER_2)
      .apiInfo(swaggerApiInfo())
      .select()
      .apis(RequestHandlerSelectors.any()).paths(PathSelectors.any())
      // .apis(RequestHandlerSelectors.basePackage("com.markruler.aptzip.controller")).paths(PathSelectors.ant("/api/**"))
      .build()
      .useDefaultResponseMessages(false);
    //@formatter:on
  }

  private ApiInfo swaggerApiInfo() {
    ApiInfoBuilder apiBuilder = new ApiInfoBuilder();
    //@formatter:off
    return apiBuilder
      .version("1.0")
      .title("Aptzip API Documentation")
      .description("앞집 API 서버 문서입니다.")
      .license("MIT")
      .licenseUrl("https://github.com/cxsu/aptzip/blob/main/LICENSE")
      .build();
    //@formatter:on
  }
}
