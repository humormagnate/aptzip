package com.example.config.common;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import com.example.config.security.LoginCheckInterceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer, WebApplicationInitializer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/home").setViewName("redirect:/");	// -> redirect 는 이동하고 나서 URL이 /로 변경
		// registry.addViewController("/home").setViewName("forward:/");// -> forward 는 이동하고 나서도 URL이 /home
		// WebMvcConfigurer.super.addViewControllers(registry);
	}

	@Override
		public void onStartup(ServletContext servletContext) throws ServletException {
		servletContext.addFilter("httpMethodFilter", HiddenHttpMethodFilter.class)
									.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginCheckInterceptor()).addPathPatterns("/go/login");
	}

}