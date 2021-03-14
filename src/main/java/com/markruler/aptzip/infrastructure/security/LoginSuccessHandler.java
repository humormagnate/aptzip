package com.markruler.aptzip.infrastructure.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
  Logger log = LoggerFactory.getLogger(LoginSuccessHandler.class);

  public LoginSuccessHandler(String defaultTargetUrl) {
    setDefaultTargetUrl(defaultTargetUrl);
  }

  @Override
  public void onAuthenticationSuccess(
  // @formatter:off
    HttpServletRequest request,
    HttpServletResponse response,
    Authentication authentication
  // @formatter:on
  ) throws ServletException, IOException {

    HttpSession session = request.getSession();

    if (session != null) {
      String redirectURL = (String) session.getAttribute("prevPage");
      if (redirectURL != null) {
        log.debug("redirectURL: {}", redirectURL);
        session.removeAttribute("prevPage");
        getRedirectStrategy().sendRedirect(request, response, redirectURL);
      } else {
        super.onAuthenticationSuccess(request, response, authentication);
      }
    } else {
      super.onAuthenticationSuccess(request, response, authentication);
    }
  }

}
