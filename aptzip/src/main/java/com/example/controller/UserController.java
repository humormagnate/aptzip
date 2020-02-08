package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
//@RequestMapping("/user/*")
public class UserController {
	
	@GetMapping(value = "/login")
	public ModelAndView loginPage(ModelAndView mv){
		log.info("pass");
		mv.setViewName("page-login");
		return mv;
	}
	
	@GetMapping(value = "/{test}")
  public ModelAndView test(@PathVariable("test") String test, ModelAndView mv){
		mv.addObject("test", test)
			.setViewName("error404");
		return mv;
  }
	
}
