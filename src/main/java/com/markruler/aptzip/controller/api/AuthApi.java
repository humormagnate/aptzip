package com.markruler.aptzip.controller.api;

import java.util.Optional;

import com.markruler.aptzip.domain.user.model.ConfirmationToken;
import com.markruler.aptzip.domain.user.model.UserAccountEntity;
import com.markruler.aptzip.domain.user.model.UserAccountRequestDto;
import com.markruler.aptzip.domain.user.service.AuthService;
import com.markruler.aptzip.domain.user.service.ConfirmationService;
import com.markruler.aptzip.domain.user.service.UserAccountService;
import com.markruler.aptzip.infrastructure.common.Constant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Controller
@Api(tags = "auth")
@RequiredArgsConstructor
public class AuthApi {
  private final AuthService authService;
  private final ConfirmationService confirmationService;
  private final UserAccountService userAccountService;
  Logger log = LoggerFactory.getLogger(AuthApi.class);

  // @Value("${spring.profiles.active}")
  // private String activeProfile;

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
      redirectAttributes.addFlashAttribute(Constant.ERROR_MESSAGE, true).addFlashAttribute(Constant.MESSAGE, "이미 가입된 이메일입니다.");
      return Constant.REDIRECT_SIGNUP_PAGE;
    }
    redirectAttributes.addFlashAttribute(Constant.SUCCESS_MESSAGE, true).addFlashAttribute(Constant.EMAIL_MESSAGE, user.getEmail());
    return Constant.REDIRECT_LOGIN_PAGE;
  }

  @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
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
        redirectAttributes.addFlashAttribute(Constant.SUCCESS_MESSAGE, true);
        return Constant.REDIRECT_LOGIN_PAGE;
      }
    }
    redirectAttributes.addFlashAttribute(Constant.MESSAGE, Constant.CONFIRM_TOKEN_NULL);
    return Constant.REDIRECT_ERROR_PAGE;
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

      redirectAttributes.addFlashAttribute(Constant.EMAIL_MESSAGE, user.getEmail());
      return Constant.REDIRECT_CONFIRM_PAGE;
    } else {
      redirectAttributes.addFlashAttribute(Constant.MESSAGE, "This email address does not exist!");
      return Constant.REDIRECT_ERROR_PAGE;
    }
  }

  @RequestMapping(value = "/confirm-reset", method = {RequestMethod.GET, RequestMethod.POST})
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
        redirectAttributes.addFlashAttribute("user", user).addFlashAttribute(Constant.EMAIL_MESSAGE, user.get().getEmail());
        return Constant.REDIRECT_RESET_PAGE;
      }
    }
    redirectAttributes.addFlashAttribute(Constant.MESSAGE, Constant.CONFIRM_TOKEN_NULL);
    return Constant.REDIRECT_ERROR_PAGE;
  }

  @PostMapping("/reset")
  public String resetUserPassword(RedirectAttributes redirectAttributes, UserAccountRequestDto user) {
    if (user.getEmail() != null) {
      Optional<UserAccountEntity> tokenUser = userAccountService.findByEmailIgnoreCase(user.getEmail());
      if (tokenUser.isPresent()) {
        userAccountService.updatePassword(user);
        redirectAttributes.addFlashAttribute(Constant.SUCCESS_MESSAGE, true);
        return Constant.REDIRECT_LOGIN_PAGE;
      }
    }
    redirectAttributes.addFlashAttribute(Constant.MESSAGE, Constant.CONFIRM_TOKEN_NULL);
    return Constant.REDIRECT_ERROR_PAGE;
  }
}
