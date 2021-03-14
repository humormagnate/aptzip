package com.markruler.aptzip.controller.view;

import com.markruler.aptzip.domain.user.service.UserAccountService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserAccountView {
  Logger log = LoggerFactory.getLogger(UserAccountView.class);

  private final UserAccountService userAccountService;

  @GetMapping("/{id}")
  public String readUserPropertyById(@PathVariable("id") Long id, Model model) {
    userAccountService.readUserPropertyById(id, model);
    return "user/page-single-user";
  }

}
