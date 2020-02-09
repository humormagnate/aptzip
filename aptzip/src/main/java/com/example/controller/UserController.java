package com.example.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.UserRequestDto;
import com.example.domain.UserResponseDto;
import com.example.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;
	
	@GetMapping(value = "/go/login")
	public ModelAndView goLogin(ModelAndView mv) {
		mv.setViewName("page-login");
		return mv;
	}

	@PostMapping(value = "/login")
	public String loginsignin(UserRequestDto user, HttpServletRequest request) {
		log.info(user.toString());
		//userService.loadUserByUsername(user.getEmail());
		String referer = request.getHeader("Referer");
		request.getSession().setAttribute("prevPage", referer);
		return "redirect:/";
	}
	
	@PostMapping(value = "/logout")
	public void logout(@RequestBody @Valid UserRequestDto user) {
		log.info("logout");
	}

	@GetMapping(value = "/go/signup")
	public String goSignup() {
		return "page-signup";
	}
	
	@PostMapping(value = "/signup")
	public String signup(@ModelAttribute @Valid UserRequestDto userForm, RedirectAttributes redirectAttributes) {
		log.info("signup : " + userForm);
		userService.save(userForm);
		redirectAttributes.addFlashAttribute("msg", "register success");
    return "redirect:go/login";
	}
	
	@GetMapping("/list")
  public List<UserResponseDto> findAll(){
      return userService.findAll();
  }

	@GetMapping(value = "/{test}")
	public ModelAndView test(@PathVariable("test") String test, ModelAndView mv) {
		mv.addObject("test", test).setViewName("error404");
		return mv;
	}

}
