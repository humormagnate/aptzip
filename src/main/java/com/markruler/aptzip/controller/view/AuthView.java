package com.markruler.aptzip.controller.view;

import com.markruler.aptzip.domain.user.model.UserAccountRequestDto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthView {

  @Value("${spring.profiles.active}")
  private String activeProfile;

  @GetMapping("/login")
  public String login() {
    return "login";
  }

  @GetMapping(value = "/signup")
  public ModelAndView displayRegistration(
  // @formatter:off
    ModelAndView modelAndView,
    UserAccountRequestDto user
  // @formatter:on
  ) {
    modelAndView.addObject("user", user);
    modelAndView.setViewName("signup");
    return modelAndView;
  }

  @GetMapping("/register")
  public String register() {
    return "user/page-register";
  }

  @GetMapping("/forgot")
  public String displayResetPassword() {
    return "user/page-forgot-password";
  }

  @GetMapping("/confirm")
  public String confimResetGet() {
    return "user/page-confirm-reset";
  }

  @GetMapping("/reset")
  public String resetUserPasswordPost() {
    return "user/page-reset-password";
  }

}
