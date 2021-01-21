package com.markruler.aptzip.config.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginCheckInterceptor extends HandlerInterceptorAdapter {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    log.debug("login check interceptor prehandle");
    
		// 이전 페이지가 회원가입, 로그인, 비밀번호 초기화, 이메일 인증 등록 페이지일 경우 메인 페이지로 리다이렉트 됩니다.
		String referrer = request.getHeader("Referer");
    if (referrer != null &&
        !referrer.endsWith("signup") &&
        !referrer.endsWith("login") &&
        !referrer.endsWith("reset") &&
        !referrer.endsWith("register")) {
      request.getSession().setAttribute("prevPage", referrer);
      log.debug("LoginCheckInterceptor prehandle save referer");
    }
    
    return super.preHandle(request, response, handler);
  }
}