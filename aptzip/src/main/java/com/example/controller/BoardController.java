package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.domain.board.BoardEntity;
import com.example.domain.user.UserResponseDto;
import com.example.persistence.BoardRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;


@Controller
@RequestMapping("/board/")
@Slf4j
public class BoardController {

	@Autowired
	private BoardRepository boardRepository;

	@Deprecated
	@GetMapping("/list")
	public ModelAndView list(ModelAndView mv) {
		List<BoardEntity> list = new ArrayList<BoardEntity>();
		for (BoardEntity str : boardRepository.findAll()) {
			list.add(str);
		}
		log.info(list.toString());
		
		mv.addObject("list", list)
			.setViewName("board/list");
		
		return mv;
	}

	@GetMapping("/write")
	public void writeGet() {}

	// AuthenticationPrincipal은 Context에 있기 때문에 계속 보내줄 필요 없음
	// public ModelAndView writeGet(@AuthenticationPrincipal UserResponseDto principal, ModelAndView mv) {
	// 	mv.addObject("principal", principal)
	// 		.setViewName("board/page-create-topic");
	// 	return mv;
	// }
	
	// UserDetails를 구현한 UserRequestDto는 @AuthenticationPrincipal 이 안먹히고
	// UserDetails를 구현한 User를 상속한 UserResponseDto는 먹힌다. Why?
	@PostMapping("/write")
	public String writePost(BoardEntity board, @AuthenticationPrincipal UserResponseDto principal, RedirectAttributes rttr) {
		
		log.info(board.toString());
		log.info(principal.toString());

		board.setBoardStatus("Y");
		board.setUser(principal.toEntity());	// java.lang.NullPointerException: null
		board.setApt(board.getUser().getApt());
		boardRepository.save(board);

		/*
			Post-Redirect-Get 방식
			: 리다이렉트를 하지 않으면 사용자가 여러 번 게시물을 등록할 수 있기 때문에 이를 방지하기 위함
		*/
		rttr.addFlashAttribute("msg", "success");

		return "redirect:/";
	}

	@GetMapping("/{id}")
	public String read(Model model, @PathVariable("id") Long id) {
		// boardRepository.findById(id).ifPresent(value -> model.addAttribute("board", value));
		boardRepository.findById(id).ifPresent(board -> {
			board.setBoardContent(board.getBoardContent().replace(System.lineSeparator(), "<br>"));
			log.info(board.toString());
			model.addAttribute("board", board);
		});
		// boardRepo.findById(id).ifPresent(value -> log.info(value.toString()));
		model.addAttribute("newLineChar", "\r");
		return "board/page-single-topic";
	}

	@GetMapping("/edit/{id}")
	public String editGet(Model model, @PathVariable("id") Long id) {
		boardRepository.findById(id).ifPresent(value -> model.addAttribute("board", value));
		return "board/write";
	}

	@PostMapping("/edit")
	public String editPost(BoardEntity board, RedirectAttributes rttr) {
		boardRepository.findById(board.getId()).ifPresent(origin -> {
			origin.setCategory(board.getCategory());
			origin.setBoardTitle(board.getBoardTitle());
			origin.setBoardContent(board.getBoardContent());
			origin.setUpdateDate(board.getUpdateDate());
			boardRepository.save(origin);
		});
		;
		return "redirect:/board/" + board.getId();
	}

	@GetMapping("/del")
	public String delGET(Long id) {
		boardRepository.deleteById(id);
		return "redirect:/";
	}

	@PostMapping(value="/search")
	public ModelAndView retrieve(ModelAndView mv) {
		return mv;
	}
	
}
