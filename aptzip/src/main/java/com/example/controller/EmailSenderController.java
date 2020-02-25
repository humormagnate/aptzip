package com.example.controller;

import com.example.domain.common.AptEntity;
import com.example.domain.user.AptzipRoleEntity;
import com.example.domain.user.AptzipUserEntity;
import com.example.domain.user.ConfirmationToken;
import com.example.domain.user.UserRole;
import com.example.persistence.user.ConfirmationTokenRepository;
import com.example.persistence.user.UserJpaRepository;
import com.example.service.EmailSenderService;
import com.example.service.UserAccountService;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
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
	public void login() {}
  
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
  public String registerUser(AptzipUserEntity user, RedirectAttributes redirectAttributes, String aptId) {
    log.info("=============================SIGN UP================================");
    // log.info("signup user : {}", user);
    AptzipUserEntity existingUser = userJpaRepository.findByEmailIgnoreCase(user.getEmail()).orElse(null);
    log.info("existing user : {}", existingUser);

    if (existingUser != null) {
      // modelAndView.addObject("message", "This email already exists!");
			redirectAttributes.addFlashAttribute("error", true);
			return "redirect:/signup";
    } else {
      
      Long apt = Long.valueOf(aptId);
      user.setApt(AptEntity.builder().id(apt).build());
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      user.setRole(new AptzipRoleEntity(UserRole.USER.name()));
      log.info("signup user change password : {}", user);
      
  		userAccountService.save(user);
  		redirectAttributes.addFlashAttribute("success", true);

      ConfirmationToken confirmationToken = new ConfirmationToken(user);

      confirmationTokenRepository.save(confirmationToken);

      SimpleMailMessage mailMessage = new SimpleMailMessage();
      mailMessage.setTo(user.getEmail());
      mailMessage.setSubject("Complete Registration!");
      mailMessage.setFrom("noreply@markruler.com");
      mailMessage.setText("To confirm your account, please click here : "
                          + "http://localhost:8888/aptzip/confirm-account?token=" + confirmationToken.getConfirmationToken());

      emailSenderService.sendEmail(mailMessage);
      log.info("email : {}", user.getEmail());
      // modelAndView.addObject("emailId", user.getEmail());
      redirectAttributes.addFlashAttribute("email", user.getEmail());
      
      return "redirect:/register";
    }
  }

  @GetMapping(value = "/register")
  public String register() {
    return "user/page-register";
  }

  @RequestMapping(value = "/confirm-account", method = { RequestMethod.GET, RequestMethod.POST })
  public String confirmUserAccount(RedirectAttributes redirectAttributes, @RequestParam("token") String confirmationToken) {
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


}