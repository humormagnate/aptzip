package com.example.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import com.example.domain.board.BoardEntity;
import com.example.domain.user.AptzipUserEntity;
import com.example.domain.user.UserRequestDto;
import com.example.domain.user.UserResponseDto;
import com.example.persistence.BoardRepository;
import com.example.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
	@PostMapping(value = "/signup")
	public String insertUser(@ModelAttribute UserRequestDto userForm, RedirectAttributes redirectAttributes) {
		log.info("=============================SIGN UP================================");
		// @Valid -> 400 error 페이지로 이동중 SecurityContext 에서 계속 405 error로 바뀐다. Why?
		try {
			userService.save(userForm);
    } catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", true);
			return "redirect:/join";
    }
		redirectAttributes.addFlashAttribute("success", true);
		return "redirect:/login";
	}

	@Deprecated
	@GetMapping("/info")
	public ModelAndView deprecatedInfo(@AuthenticationPrincipal UserResponseDto principal, ModelAndView mv) {
		// Principal principal = request.getUserPrincipal();
		List<BoardEntity> list = boardRepository.findByUserIdOrderByIdDesc(principal.getId());
		log.info(list.toString());
		
		mv.addObject("principal", principal)
			.addObject("list", list)
			.setViewName("user/page-single-user");
		return mv;
	}

	@GetMapping("/{id}/info")
	public ModelAndView info(@PathVariable("id") Long id, ModelAndView mv) {
		List<BoardEntity> list = boardRepository.findByUserIdOrderByIdDesc(id);
		AptzipUserEntity user = userService.findById(id);
		log.info(list.toString());
		
		mv.addObject("list", list)
			.addObject("infouser", user)
			.setViewName("user/page-single-user");
		return mv;
	}

	// @ResponseBody // -> 415 error
	@Transactional
	@PutMapping("/edit")
	public ResponseEntity<List<String>> updateUser(@RequestBody UserRequestDto user, HttpServletRequest request) {
		log.info("user : " + user);
		log.info("request : " + request);

		// request.getSession().setAttribute("msg", "비번변경");
		// ServerRequest request -> java.lang.NoSuchMethodException: org.springframework.web.servlet.function.ServerRequest.<init>()
		// request.session().setAttribute("msg", "비밀번호가 변경되었습니다. 다시 로그인해주세요.");

		return new ResponseEntity<>(Arrays.asList("success"), HttpStatus.OK);
	}

	@GetMapping("/{id}/message")
	public String message() {
		return "user/messages-page";
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

	// @GetMapping(value = "/{test}")
	// public ModelAndView test(@PathVariable("test") String test, ModelAndView mv) {
	// 	mv.addObject("test", test).setViewName("error404");
	// 	return mv;
	// }

}
