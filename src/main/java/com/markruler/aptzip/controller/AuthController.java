package com.markruler.aptzip.controller;

import java.util.Optional;

import com.markruler.aptzip.domain.user.ConfirmationToken;
import com.markruler.aptzip.domain.user.UserAccountEntity;
import com.markruler.aptzip.domain.user.UserAccountRequestDto;
import com.markruler.aptzip.service.AuthService;
import com.markruler.aptzip.service.ConfirmationService;
import com.markruler.aptzip.service.UserAccountService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Controller
@Api(tags = "auth")
@RequiredArgsConstructor
public class AuthController {
  Logger log = LoggerFactory.getLogger(AuthController.class);

  private final AuthService authService;
  private final ConfirmationService confirmationService;
  private final UserAccountService userAccountService;

  private final String CONFIRM_TOKEN_NULL = "The link is invalid or broken!";
  private final String REDIRECT_ERROR_PAGE = "redirect:/error";
  private final String REDIRECT_LOGIN_PAGE = "redirect:/login";
  private final String SUCCESS_MESSAGE = "success";
  private final String EMAIL_MESSAGE = "email";

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
  public String displayResetPassword(Model model, UserAccountRequestDto user) {
    model.addAttribute("user", user);
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

  @ApiOperation(value = "회원가입")
  @PostMapping(value = "/signup", consumes = "application/x-www-form-urlencoded")
  public String registerUser(
  // @formatter:off
    @ModelAttribute UserAccountRequestDto user,
    RedirectAttributes redirectAttributes,
    String aptCode
  // @formatter:on
  ) {
    log.debug("apartment code: {}", aptCode);
    UserAccountEntity returnedUser = userAccountService.save(user, aptCode);
    if (returnedUser == null) {
      redirectAttributes.addFlashAttribute("error", true).addFlashAttribute("message", "이미 가입된 이메일입니다.");
      return "redirect:/signup";
    }
    redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, true).addFlashAttribute(EMAIL_MESSAGE, user.getEmail());
    return REDIRECT_LOGIN_PAGE;
  }

  @RequestMapping(value = "/confirm-account", method = { RequestMethod.GET, RequestMethod.POST })
  public String confirmUserAccount(
  // @formatter:off
    RedirectAttributes redirectAttributes,
    @RequestParam("token") String token
  // @formatter:on
  ) {
    ConfirmationToken confirmationToken = confirmationService.findByToken(token);
    if (confirmationToken != null) {
      Optional<UserAccountEntity> user = userAccountService
          .findByEmailIgnoreCase(confirmationToken.getUser().getEmail());
      if (user.isPresent()) {
        userAccountService.enabledUser(user.get().getId());
        redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, true);
        return REDIRECT_LOGIN_PAGE;
      }
    }
    redirectAttributes.addFlashAttribute("msg", CONFIRM_TOKEN_NULL);
    return "redirect:/error";
  }

  @PostMapping("/forgot")
  public String forgotUserPassword(RedirectAttributes redirectAttributes, UserAccountRequestDto user) {
    Optional<UserAccountEntity> existingUser = userAccountService.findByEmailIgnoreCase(user.getEmail());

    if (existingUser.isPresent()) {
      ConfirmationToken confirmationToken = confirmationService.save(existingUser.get());

      SimpleMailMessage mailMessage = new SimpleMailMessage();
      mailMessage.setTo(existingUser.get().getEmail());
      mailMessage.setSubject("Complete Password Reset!");
      mailMessage.setFrom("markrulerofficial@gmail.com");
      mailMessage.setText("To complete the password reset process, please click here: "
          + "https://markruler.com/aptzip/confirm-reset?token=" + confirmationToken.getToken());

      authService.sendEmail(mailMessage);

      redirectAttributes.addFlashAttribute(EMAIL_MESSAGE, user.getEmail());
      return "redirect:/confirm";
    } else {
      redirectAttributes.addFlashAttribute("msg", "This email address does not exist!");
      return REDIRECT_ERROR_PAGE;
    }
  }

  @RequestMapping(value = "/confirm-reset", method = { RequestMethod.GET, RequestMethod.POST })
  public String validateResetToken(
  // @formatter:off
    RedirectAttributes redirectAttributes,
    @RequestParam("token") String token
  // @formatter:on
  ) {
    ConfirmationToken confirmationToken = confirmationService.findByToken(token);
    if (confirmationToken != null) {
      Optional<UserAccountEntity> user = userAccountService
          .findByEmailIgnoreCase(confirmationToken.getUser().getEmail());
      if (user.isPresent()) {
        userAccountService.enabledUser(user.get().getId());
        redirectAttributes.addFlashAttribute("user", user).addFlashAttribute(EMAIL_MESSAGE, user.get().getEmail());
        return "redirect:/reset";
      }
    }
    redirectAttributes.addFlashAttribute("msg", CONFIRM_TOKEN_NULL);
    return REDIRECT_ERROR_PAGE;
  }

  @PostMapping("/reset")
  public String resetUserPassword(RedirectAttributes redirectAttributes, UserAccountRequestDto user) {
    if (user.getEmail() != null) {
      Optional<UserAccountEntity> tokenUser = userAccountService.findByEmailIgnoreCase(user.getEmail());
      if (tokenUser.isPresent()) {
        userAccountService.updatePassword(user);
        redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, true);
        return REDIRECT_LOGIN_PAGE;
      }
    }
    redirectAttributes.addFlashAttribute("msg", CONFIRM_TOKEN_NULL);
    return REDIRECT_ERROR_PAGE;
  }
}
