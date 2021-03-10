package com.markruler.aptzip.controller;

import java.util.Optional;

import com.markruler.aptzip.domain.user.UserAccountEntity;
import com.markruler.aptzip.domain.user.ConfirmationToken;
import com.markruler.aptzip.domain.user.UserRequestDto;
import com.markruler.aptzip.service.AuthService;
import com.markruler.aptzip.service.ConfirmationService;
import com.markruler.aptzip.service.UserAccountService;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;
  private final ConfirmationService confirmationService;
  private final UserAccountService userAccountService;

  @GetMapping("/login")
  public String login() {
    return "login";
  }

  @GetMapping(value = "/signup")
  public ModelAndView displayRegistration(ModelAndView modelAndView, UserRequestDto user) {
    modelAndView.addObject("user", user);
    modelAndView.setViewName("signup");
    return modelAndView;
  }

  @PostMapping(value = "/signup")
  public String registerUser(@RequestBody UserRequestDto user, RedirectAttributes redirectAttributes,
      String aptCode/* , ConnectionData connection */) {
    log.debug("apartment code: {}", aptCode);
    UserAccountEntity returnedUser = userAccountService.save(user, aptCode);
    if (returnedUser == null) {
      redirectAttributes.addFlashAttribute("error", true).addFlashAttribute("message", "이미 가입된 이메일입니다.");
      return "redirect:/signup";
    }
    redirectAttributes.addFlashAttribute("success", true);

    // 실제 서비스가 아니라면 개발 환경에서 오류가 발생하므로 주석 처리
    /*
     * ConfirmationToken confirmationToken = confirmationService.createToken(user);
     *
     * SimpleMailMessage mailMessage = new SimpleMailMessage();
     * mailMessage.setTo(user.getEmail());
     * mailMessage.setSubject("Complete Registration!");
     * mailMessage.setFrom("noreply@markruler.com");
     * mailMessage.setText("To confirm your account, please click here : " +
     * "https://markruler.com/aptzip/confirm-account?token=" +
     * confirmationToken.getConfirmationToken());
     *
     * emailSenderService.sendEmail(mailMessage);
     * redirectAttributes.addFlashAttribute("email", user.getEmail());
     */
    return "redirect:/login";
  }

  // 로그인 페이지 redirect를 피하려고 만든 컨트롤러
  // 그렇지 않으면 매핑 경로가 아닌 HTML 파일 경로를 적어줘야 합니다.
  @GetMapping(value = "/register")
  public String register() {
    return "user/page-register";
  }

  @Deprecated
  @RequestMapping(value = "/confirm-account", method = { RequestMethod.GET, RequestMethod.POST })
  public String confirmUserAccount(RedirectAttributes redirectAttributes,
      @RequestParam("token") String confirmationToken) {
    ConfirmationToken token = confirmationService.findToken(confirmationToken);

    if (token != null) {
      Optional<UserAccountEntity> user = userAccountService.findByEmailIgnoreCase(token.getUser().getEmail());
      if (user.isPresent()) {
        userAccountService.enabledUser(user.get().getId());
        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/login";
      }
    }
    redirectAttributes.addFlashAttribute("msg", "The link is invalid or broken!");
    return "redirect:/error";
  }

  @GetMapping(value = "/forgot")
  public String displayResetPassword(Model model, UserRequestDto user) {
    model.addAttribute("user", user);
    return "user/page-forgot-password";
  }

  @Deprecated
  @PostMapping(value = "/forgot")
  public String forgotUserPassword(RedirectAttributes redirectAttributes, UserRequestDto user) {
    Optional<UserAccountEntity> existingUser = userAccountService.findByEmailIgnoreCase(user.getEmail());

    if (existingUser.isPresent()) {
      ConfirmationToken confirmationToken = confirmationService.createToken(existingUser.get());

      SimpleMailMessage mailMessage = new SimpleMailMessage();
      mailMessage.setTo(existingUser.get().getEmail());
      mailMessage.setSubject("Complete Password Reset!");
      mailMessage.setFrom("markrulerofficial@gmail.com");
      mailMessage.setText("To complete the password reset process, please click here: "
          + "https://markruler.com/aptzip/confirm-reset?token=" + confirmationToken.getConfirmationToken());

      authService.sendEmail(mailMessage);

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

  @Deprecated
  @RequestMapping(value = "/confirm-reset", method = { RequestMethod.GET, RequestMethod.POST })
  public String validateResetToken(RedirectAttributes redirectAttributes,
      @RequestParam("token") String confirmationToken) {
    ConfirmationToken token = confirmationService.findToken(confirmationToken);

    if (token != null) {
      Optional<UserAccountEntity> user = userAccountService.findByEmailIgnoreCase(token.getUser().getEmail());
      if (user.isPresent()) {
        userAccountService.enabledUser(user.get().getId());
        redirectAttributes.addFlashAttribute("user", user).addFlashAttribute("email", user.get().getEmail());
        return "redirect:/reset";
      }
    }
    redirectAttributes.addFlashAttribute("msg", "The link is invalid or broken!");
    return "redirect:/error";
  }

  @GetMapping("/reset")
  public String resetUserPasswordPost() {
    return "user/page-reset-password";
  }

  @Deprecated(forRemoval = false)
  @PostMapping(value = "/reset")
  public String resetUserPassword(RedirectAttributes redirectAttributes, UserRequestDto user) {
    if (user.getEmail() != null) {
      Optional<UserAccountEntity> tokenUser = userAccountService.findByEmailIgnoreCase(user.getEmail());
      if (tokenUser.isPresent()) {
        userAccountService.updatePassword(user);
        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/login";
      }
    }
    redirectAttributes.addFlashAttribute("msg", "The link is invalid or broken!");
    return "redirect:/error";
  }
}
