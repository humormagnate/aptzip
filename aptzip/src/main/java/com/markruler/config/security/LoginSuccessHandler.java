package com.markruler.config.security;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@Slf4j
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
  
  public LoginSuccessHandler(String defaultTargetUrl) {
    setDefaultTargetUrl(defaultTargetUrl);
    log.info("===============================LoginSuccessHandler-Constructor=====================================");
  }

  @Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
                                              Authentication authentication) throws ServletException, IOException {
      
    log.info(request.toString());
    log.info("===============================LoginSuccessHandler-onAuthenticationSuccess=====================================");
    // ((SecurityMember)authentication.getPrincipal()).setIp(getClientIp(request));
    
    HttpSession session = request.getSession();

    /**
     * 로그인에 성공할 경우
     * LoginCheckInterceptor에서 지정한 페이지로 리다이렉트됩니다.
     */
    if (session != null) {
        String redirectUrl = (String) session.getAttribute("prevPage");
        if (redirectUrl != null) {
            session.removeAttribute("prevPage");
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    } else {
        super.onAuthenticationSuccess(request, response, authentication);
    }
  }

}