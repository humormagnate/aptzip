package com.markruler.controller;

import com.markruler.domain.common.AptEntity;
import com.markruler.domain.user.AptzipRoleEntity;
import com.markruler.domain.user.AptzipUserEntity;
import com.markruler.domain.user.ConfirmationToken;
import com.markruler.domain.user.UserRole;
import com.markruler.persistence.user.ConfirmationTokenRepository;
import com.markruler.persistence.user.UserJpaRepository;
import com.markruler.service.EmailSenderService;
import com.markruler.service.UserAccountService;

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

  // https://www.baeldung.com/spring-email
  // https://stackabuse.com/how-to-send-emails-in-java/
  // https://stackabuse.com/spring-security-email-verification-registration/
  // https://www.baeldung.com/registration-with-spring-mvc-and-spring-security
  @PostMapping(value = "/signup")
  public String registerUser(AptzipUserEntity user, RedirectAttributes redirectAttributes, String aptId, ConnectionData connection) {
    log.info("=============================SIGN UP================================");
    AptzipUserEntity existingUser = userJpaRepository.findByEmailIgnoreCase(user.getEmail()).orElse(null);
    log.info("existing user : {}", existingUser);
    
    if (existingUser != null) {
      log.info("existingUser is not null");

      // modelAndView.addObject("message", "This email already exists!");
      redirectAttributes.addFlashAttribute("error", true);
      return "redirect:/signup";
    } else {
      log.info("existingUser is null");
      
      log.info("connection data : {}", connection);

      Long apt = Long.valueOf(aptId);
      user.setApt(AptEntity.builder().id(apt).build());
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      user.setRole(new AptzipRoleEntity(UserRole.USER.name()));

      log.info("signup user change password : {}", user);
      
      if (connection != null) {
        log.info("connection data is not null");
        user.setProviderId(connection.getProviderId());
        user.setProviderUserId(connection.getProviderUserId());
      }

      log.info("signup user register connection data : {}", user);
      
      userAccountService.save(user);
      redirectAttributes.addFlashAttribute("success", true);

      ConfirmationToken confirmationToken = new ConfirmationToken(user);

      confirmationTokenRepository.save(confirmationToken);

      SimpleMailMessage mailMessage = new SimpleMailMessage();
      mailMessage.setTo(user.getEmail());
      mailMessage.setSubject("Complete Registration!");
      mailMessage.setFrom("noreply@markruler.com");
      mailMessage.setText("To confirm your account, please click here : "
          + "https://markruler.com/aptzip/confirm-account?token=" + confirmationToken.getConfirmationToken());

      emailSenderService.sendEmail(mailMessage);
      log.info("email : {}", user.getEmail());
      // modelAndView.addObject("emailId", user.getEmail());
      redirectAttributes.addFlashAttribute("email", user.getEmail());

      // return "redirect:user/page-register";  // -> SecurityConfig에서 /user/* 페이지에 권한을 요구하기 때문에 로그인 페이지로 redirect...
      return "redirect:/register";
    }
  }

  // 로그인 페이지 redirect를 피하려고 만든 get mapping
  // 안그러면 매핑 경로가 아닌 html 경로를 적어줘야함
  @GetMapping(value = "/register")
  public String register() {
    return "user/page-register";
  }

  @RequestMapping(value = "/confirm-account", method = { RequestMethod.GET, RequestMethod.POST })
  public String confirmUserAccount(RedirectAttributes redirectAttributes, @RequestParam("token") String confirmationToken) {
    log.info("================================= confirm-account =================================");
    ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

    if (token != null) {
      AptzipUserEntity user = userJpaRepository.findByEmailIgnoreCase(token.getUser().getEmail()).orElse(null);

      user.setEnabled(true);
      userJpaRepository.save(user);
      // redirect하면 model attribute는 포함되지 않음.
      // model.addAttribute("success", true);
      redirectAttributes.addFlashAttribute("success", true);
      return "redirect:/login";
    } else {
      // modelAndView.addObject("message", "The link is invalid or broken!");
      // model.addAttribute("msg", "The link is invalid or broken!");
      redirectAttributes.addFlashAttribute("msg", "The link is invalid or broken!");
    }
    return "redirect:/error";
  }

  // https://stackabuse.com/spring-security-forgot-password-functionality/
  // Display the form
  @GetMapping(value = "/forgot")
  public String displayResetPassword(Model model, AptzipUserEntity user) {
    model.addAttribute("user", user);
    return "user/page-forgot-password";
  }

  // Receive the address and send an email
  @PostMapping(value = "/forgot")
  public String forgotUserPassword(RedirectAttributes redirectAttributes, AptzipUserEntity user) {
    log.info("================================= /forgot =================================");
    AptzipUserEntity existingUser = userJpaRepository.findByEmailIgnoreCase(user.getEmail()).orElse(null);
    if (existingUser != null) {
      // Create token
      ConfirmationToken confirmationToken = new ConfirmationToken(existingUser);

      // Save it
      confirmationTokenRepository.save(confirmationToken);

      // Create the email
      SimpleMailMessage mailMessage = new SimpleMailMessage();
      mailMessage.setTo(existingUser.getEmail());
      mailMessage.setSubject("Complete Password Reset!");
      mailMessage.setFrom("markrulerofficial@gmail.com");
      mailMessage.setText("To complete the password reset process, please click here: "
          + "https://markruler.com/aptzip/confirm-reset?token=" + confirmationToken.getConfirmationToken());

      // Send the email
      emailSenderService.sendEmail(mailMessage);

      // modelAndView.addObject("message", "Request to reset password received. Check your inbox for the reset link.");
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

  // Endpoint to confirm the token
  // handles the verification of the token and on success will redirect the user to the next endpoint
  @RequestMapping(value = "/confirm-reset", method = { RequestMethod.GET, RequestMethod.POST })
  public String validateResetToken(RedirectAttributes redirectAttributes, @RequestParam("token") String confirmationToken) {
    log.info("================================= reset : validate reset token =================================");
    ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

    if (token != null) {
      AptzipUserEntity user = userJpaRepository.findByEmailIgnoreCase(token.getUser().getEmail()).orElse(null);
      user.setEnabled(true);
      userJpaRepository.save(user);
      redirectAttributes
        .addFlashAttribute("user", user)
        .addFlashAttribute("email", user.getEmail());
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

  // Endpoint to update a user's password
  // displays the form above, receives the new credentials, and update them in the database
  @PostMapping(value = "/reset")
  public String resetUserPassword(RedirectAttributes redirectAttributes, AptzipUserEntity user) {
    log.info("================================= reset-password =================================");
    log.info("user : {}", user);

    if (user.getEmail() != null) {
      // Use email to find user
      AptzipUserEntity tokenUser = userJpaRepository.findByEmailIgnoreCase(user.getEmail()).orElse(null);
      tokenUser.setPassword(passwordEncoder.encode(user.getPassword()));
      userJpaRepository.save(tokenUser);
      // modelAndView.addObject("message", "Password successfully reset. You can now log in with the new credentials.");
      redirectAttributes.addFlashAttribute("success", true);
      return "redirect:/login";
    } else {
      // modelAndView.addObject("message", "The link is invalid or broken!");
      redirectAttributes.addFlashAttribute("msg", "The link is invalid or broken!");
      return "redirect:/error";
    }
  }

}