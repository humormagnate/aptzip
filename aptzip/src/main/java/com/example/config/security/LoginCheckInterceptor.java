package com.example.config.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginCheckInterceptor extends HandlerInterceptorAdapter {

  /**
   * This implementation always returns {@code true}.
   */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    log.info("login check interceptor prehandle");
    
		// not redirect signup page, login page again
		String referrer = request.getHeader("Referer");
    if (referrer != null &&
        !referrer.endsWith("signup") &&
        !referrer.endsWith("login") &&
        !referrer.endsWith("register")) {
      request.getSession().setAttribute("prevPage", referrer);
      log.info("LoginCheckInterceptor prehandle save referer");
    }
    
    return super.preHandle(request, response, handler);
  }
}