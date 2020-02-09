package com.example.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.UserRequestDto;
import com.example.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping(value = "/go/login")
	public ModelAndView goLogin(ModelAndView mv) {
		mv.setViewName("user/page-login");
		return mv;
	}
	
	@GetMapping(value = "/go/signup")
	public String goSignup() {
		return "user/page-signup";
	}
	
	@PostMapping(value = "/signup")
	public String signup(@ModelAttribute @Valid UserRequestDto userForm, RedirectAttributes redirectAttributes) {
		log.info("=============================SIGN UP================================");
		log.info("signup : " + userForm);
		userService.save(userForm);
		redirectAttributes.addFlashAttribute("success", true);
    return "redirect:go/login";
	}
	
	@GetMapping("/user/info")
	public String userInfo() {
		return "user/page-single-user";
	}

	@GetMapping(value = "/{test}")
	public ModelAndView test(@PathVariable("test") String test, ModelAndView mv) {
		mv.addObject("test", test).setViewName("error404");
		return mv;
	}

}
