package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.domain.board.BoardEntity;
import com.example.domain.user.UserResponseDto;
import com.example.persistence.BoardRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AptzipCommonController {
	
	@Autowired
	private BoardRepository boardRepository;

	@GetMapping("/")
  //public ModelAndView home(Principal principal, ModelAndView mv) {
  public ModelAndView home(@AuthenticationPrincipal UserResponseDto principal, ModelAndView mv) {
		// https://itstory.tk/entry/Spring-Security-%ED%98%84%EC%9E%AC-%EB%A1%9C%EA%B7%B8%EC%9D%B8%ED%95%9C-%EC%82%AC%EC%9A%A9%EC%9E%90-%EC%A0%95%EB%B3%B4-%EA%B0%80%EC%A0%B8%EC%98%A4%EA%B8%B0
		// https://lemontia.tistory.com/602
		log.info(principal + "=========================================================");

		List<BoardEntity> list = new ArrayList<BoardEntity>();
		for (BoardEntity str : boardRepository.findAll()) {
			list.add(str);
		}
		// log.info(list.toString());
		
		mv.addObject("principal", principal)
			.addObject("list", list)
			.setViewName("index");
		
		return mv;
	}
	
}
