package com.markruler.aptzip.controller;

import com.markruler.aptzip.domain.apartment.AptEntity;
import com.markruler.aptzip.domain.user.AptzipRoleEntity;
import com.markruler.aptzip.domain.user.AptzipUserEntity;
import com.markruler.aptzip.domain.user.ConfirmationToken;
import com.markruler.aptzip.domain.user.UserRole;
import com.markruler.aptzip.persistence.user.ConfirmationTokenRepository;
import com.markruler.aptzip.persistence.user.UserJpaRepository;
import com.markruler.aptzip.service.EmailSenderService;
import com.markruler.aptzip.service.UserAccountService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.ConnectionData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class EmailSenderController {

  private final UserJpaRepository userJpaRepository;
  private final ConfirmationTokenRepository confirmationTokenRepository;
  private final UserAccountService userAccountService;
  private final PasswordEncoder passwordEncoder;
  private final EmailSenderService emailSenderService;

  @GetMapping("/login")
  public void login() {
  }

  @GetMapping(value = "/signup")
  public ModelAndView displayRegistration(ModelAndView modelAndView, AptzipUserEntity user) {
    modelAndView.addObject("user", user);
    modelAndView.setViewName("signup");
    return modelAndView;
  }

  @PostMapping(value = "/signup")
  public String registerUser(AptzipUserEntity user, RedirectAttributes redirectAttributes,
      String aptId, ConnectionData connection) {
    AptzipUserEntity existingUser =
        userJpaRepository.findByEmailIgnoreCase(user.getEmail()).orElse(null);

    if (existingUser != null) {

      redirectAttributes.addFlashAttribute("error", true);
      return "redirect:/signup";
    } else {
      log.info("User Not Found");

      Long apt = Long.valueOf(aptId);
      user.setApt(AptEntity.builder().id(apt).build());
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      user.setRole(new AptzipRoleEntity(UserRole.USER.name()));

      if (connection != null) {
        user.setProviderId(connection.getProviderId());
        user.setProviderUserId(connection.getProviderUserId());
      }

      userAccountService.save(user);
      redirectAttributes.addFlashAttribute("success", true);

      ConfirmationToken confirmationToken = new ConfirmationToken(user);

      confirmationTokenRepository.save(confirmationToken);

      SimpleMailMessage mailMessage = new SimpleMailMessage();
      mailMessage.setTo(user.getEmail());
      mailMessage.setSubject("Complete Registration!");
      mailMessage.setFrom("noreply@markruler.com");
      mailMessage.setText("To confirm your account, please click here : "
          + "https://markruler.com/aptzip/confirm-account?token="
          + confirmationToken.getConfirmationToken());

      emailSenderService.sendEmail(mailMessage);
      redirectAttributes.addFlashAttribute("email", user.getEmail());

      return "redirect:/register";
    }
  }

  // 로그인 페이지 redirect를 피하려고 만든 컨트롤러
  // 그렇지 않으면 매핑 경로가 아닌 HTML 파일 경로를 적어줘야 합니다.
  @GetMapping(value = "/register")
  public String register() {
    return "user/page-register";
  }

  @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
  public String confirmUserAccount(RedirectAttributes redirectAttributes,
      @RequestParam("token") String confirmationToken) {
    ConfirmationToken token =
        confirmationTokenRepository.findByConfirmationToken(confirmationToken);

    if (token != null) {
      AptzipUserEntity user =
          userJpaRepository.findByEmailIgnoreCase(token.getUser().getEmail()).orElse(null);

      user.setEnabled(true);
      userJpaRepository.save(user);
      redirectAttributes.addFlashAttribute("success", true);
      return "redirect:/login";
    } else {
      redirectAttributes.addFlashAttribute("msg", "The link is invalid or broken!");
    }
    return "redirect:/error";
  }

  @GetMapping(value = "/forgot")
  public String displayResetPassword(Model model, AptzipUserEntity user) {
    model.addAttribute("user", user);
    return "user/page-forgot-password";
  }

  @PostMapping(value = "/forgot")
  public String forgotUserPassword(RedirectAttributes redirectAttributes, AptzipUserEntity user) {
    AptzipUserEntity existingUser =
        userJpaRepository.findByEmailIgnoreCase(user.getEmail()).orElse(null);

    if (existingUser != null) {
      ConfirmationToken confirmationToken = new ConfirmationToken(existingUser);
      confirmationTokenRepository.save(confirmationToken);

      SimpleMailMessage mailMessage = new SimpleMailMessage();
      mailMessage.setTo(existingUser.getEmail());
      mailMessage.setSubject("Complete Password Reset!");
      mailMessage.setFrom("markrulerofficial@gmail.com");
      mailMessage.setText("To complete the password reset process, please click here: "
          + "https://markruler.com/aptzip/confirm-reset?token="
          + confirmationToken.getConfirmationToken());

      emailSenderService.sendEmail(mailMessage);

      redirectAttributes.addFlashAttribute("email", user.getEmail());
      return "redirect:/confirm";
    } else {
      redirectAttributes.addFlashAttribute("msg", "This email address does not exist!");
      return "redirect:/error";
    }
  }

  @GetMapping("/confirm")
  public String confimResetGet() {
    return "user/page-confirm-reset";
  }

  @RequestMapping(value = "/confirm-reset", method = {RequestMethod.GET, RequestMethod.POST})
  public String validateResetToken(RedirectAttributes redirectAttributes,
      @RequestParam("token") String confirmationToken) {
    ConfirmationToken token =
        confirmationTokenRepository.findByConfirmationToken(confirmationToken);

    if (token != null) {
      AptzipUserEntity user =
          userJpaRepository.findByEmailIgnoreCase(token.getUser().getEmail()).orElse(null);
      user.setEnabled(true);
      userJpaRepository.save(user);
      redirectAttributes.addFlashAttribute("user", user).addFlashAttribute("email",
          user.getEmail());
      return "redirect:/reset";
    } else {
      redirectAttributes.addFlashAttribute("msg", "The link is invalid or broken!");
      return "redirect:/error";
    }
  }

  @GetMapping("/reset")
  public String resetUserPasswordPost() {
    return "user/page-reset-password";
  }

  @PostMapping(value = "/reset")
  public String resetUserPassword(RedirectAttributes redirectAttributes, AptzipUserEntity user) {
    if (user.getEmail() != null) {
      AptzipUserEntity tokenUser =
          userJpaRepository.findByEmailIgnoreCase(user.getEmail()).orElse(null);
      tokenUser.setPassword(passwordEncoder.encode(user.getPassword()));
      userJpaRepository.save(tokenUser);
      redirectAttributes.addFlashAttribute("success", true);
      return "redirect:/login";
    } else {
      redirectAttributes.addFlashAttribute("msg", "The link is invalid or broken!");
      return "redirect:/error";
    }
  }

}
