package com.markruler.aptzip.config.common;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import com.markruler.aptzip.config.security.LoginCheckInterceptor;

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
		registry.addViewController("/home").setViewName("redirect:/");
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		servletContext.addFilter("httpMethodFilter", HiddenHttpMethodFilter.class)
				.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginCheckInterceptor()).addPathPatterns("/login");
	}

}