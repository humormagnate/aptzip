package com.markruler.aptzip.infrastructure.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginCheckInterceptor extends HandlerInterceptorAdapter {
  Logger log = LoggerFactory.getLogger(LoginCheckInterceptor.class);

  @Override
  public boolean preHandle(
  // @formatter:off
    HttpServletRequest request,
    HttpServletResponse response,
    Object handler
  // @formatter:on
  ) throws Exception {
    log.debug("login check interceptor prehandle");

    String referrer = request.getHeader("Referer");
    log.debug("Referer: {}", referrer);

    if (
    // @formatter:off
      referrer != null &&
      !referrer.endsWith("signup") &&
      !referrer.endsWith("login") &&
      !referrer.endsWith("reset") &&
      !referrer.endsWith("register")
    // @formatter:on
    ) {
      request.getSession().setAttribute("prevPage", referrer);
      log.debug("LoginCheckInterceptor prehandle save referer");
    }

    return super.preHandle(request, response, handler);
  }
}
