package com.example.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.config.thymeleaf.expression.TemporalsAptzip;
import com.example.domain.board.BoardEntity;
import com.example.domain.common.AptEntity;
import com.example.domain.user.UserResponseDto;
import com.example.persistence.board.BoardRepository;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/apt/")  // 공통으로 PathVariable을 줄 수 없음(ex. "/apt/{id}")
@Controller
public class AptController {

  private final BoardRepository boardRepo;

  @GetMapping("/{id}")
  public String thread(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal UserResponseDto principal) {
    // apt id를 받아 해당 아파트의 thread만 받음
    log.info(principal + "=========================================================");

    List<BoardEntity> list = new ArrayList<BoardEntity>();
    AptEntity apt = new AptEntity(id);
		Iterable<BoardEntity> board = boardRepo.findAllByAptOrderByIdDesc(apt);
		int newBoard = 0;

		for (BoardEntity str : board) {
			list.add(str);
			if (new TemporalsAptzip(Locale.KOREA).isItOneHourAgo(str.getCreateDate())) {
				newBoard++;
			}
		}
		log.info("list.size() : " + list.size());
		log.info("list : " + list.toString());
		
    model.addAttribute("principal", principal)
         .addAttribute("list", list)
         .addAttribute("newBoard", newBoard);
		
		return "index";
  }
}