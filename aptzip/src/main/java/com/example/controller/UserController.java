package com.example.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.example.domain.board.BoardEntity;
import com.example.domain.user.UserRequestDto;
import com.example.domain.user.UserResponseDto;
import com.example.persistence.BoardRepository;
import com.example.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private BoardRepository boardRepository;
	
	@GetMapping(value = "/go/login")
	public ModelAndView login(ModelAndView mv, HttpServletRequest request) {
		
		// not signup page
		String referrer = request.getHeader("Referer");
		if (!referrer.endsWith("signup")) {
			request.getSession().setAttribute("prevPage", referrer);
		}
		
		mv.setViewName("user/page-login");
		return mv;
	}
	
	@GetMapping(value = "/go/signup")
	public String signup() {
		return "user/page-signup";
	}
	
	@PostMapping(value = "/signup")
	public String insertUser(@ModelAttribute @Valid UserRequestDto userForm, RedirectAttributes redirectAttributes) {
		log.info("=============================SIGN UP================================");
		log.info("signup : " + userForm);
		userService.createUser(userForm);
		redirectAttributes.addFlashAttribute("success", true);
    return "redirect:go/login";
	}

	@GetMapping("/info")
	public ModelAndView info(@AuthenticationPrincipal UserResponseDto principal, ModelAndView mv) {
		// Principal principal = request.getUserPrincipal();
		List<BoardEntity> list = boardRepository.findByUserId(principal.getId());
		log.info(list.toString());

		mv.addObject("principal", principal)
			.addObject("list", list)
			.setViewName("user/page-single-user");
		return mv;
	}

	@ResponseBody
	@GetMapping("/edit")
	public String updateUser(HttpServletRequest request, UserRequestDto user) {
		userService.updateUser(user);
		return "";
	}

	// @PostMapping("/users/{userId}")
	// @PreAuthorize("#updateUser.email == authentication.name")
	// public String update(@PathVariable("userId") Long id, @ModelAttribute @Valid UserRequestDto updateUser, Model model) {
	// 	AptzipUserEntity currentUser = userService.findOne(id);
	// 	if (!passwordEncoder.matches(updateUser.getPassword(), currentUser.getPassword())){
	// 		throw new RuntimeException("Not password equals...");
	// 	}
	// 	return "";
	// }

	@GetMapping(value = "/{test}")
	public ModelAndView test(@PathVariable("test") String test, ModelAndView mv) {
		mv.addObject("test", test).setViewName("error404");
		return mv;
	}

}
