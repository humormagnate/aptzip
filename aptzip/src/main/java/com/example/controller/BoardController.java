package com.example.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import com.example.domain.board.BoardEntity;
import com.example.persistence.BoardRepository;

import org.springframework.beans.factory.annotation.Autowired;
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
	private BoardRepository boardRepo;

	// private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	@Deprecated
	@GetMapping("/list")
	public ModelAndView list(ModelAndView mv) {
		List<BoardEntity> list = new ArrayList<BoardEntity>();
		for (BoardEntity str : boardRepo.findAll()) {
			list.add(str);
		}
		log.info(list.toString());
		
		mv.addObject("list", list)
			.setViewName("board/list");
		
		return mv;
	}

	@GetMapping("/write")
	public ModelAndView writeGet(Principal principal, ModelAndView mv) {
		// log.info("principal : " + principal);
		mv.addObject("principal", principal)
			.setViewName("board/page-create-topic");
		return mv;
	}

	@PostMapping("/write")
	public String writePost(BoardEntity board) {
		// Date now = new Date();
		// b.setCreateDate(sdf.format(now));
		// b.setUpdateDate(sdf.format(now));
		// b.setBoardStatus("N");
		log.info(board.toString());
		boardRepo.save(board);
		return "redirect:/";
	}

	@GetMapping("/{id}")
	public String get(Model model, @PathVariable("id") Long id) {
		boardRepo.findById(id).ifPresent(board -> model.addAttribute("board", board));
		// boardRepo.findById(id).ifPresent(board -> log.info(board.toString()));
		return "board/page-single-topic";
	}

	@GetMapping("/edit")
	public String editGET(Long bid, Model model) {
		boardRepo.findById(bid).ifPresent(board -> model.addAttribute("board", board));
		return "board/write";
	}

	@PostMapping("/edit")
	public String editPOST(BoardEntity board, RedirectAttributes rttr) {
		// Date now = new Date();
		// b.setUpdateDate(sdf.format(now));
		// b.setBoardStatus("N");

		boardRepo.findById(board.getId()).ifPresent(origin -> {
			origin.setCategory(board.getCategory());
			origin.setBoardTitle(board.getBoardTitle());
			origin.setBoardContent(board.getBoardContent());
			origin.setUpdateDate(board.getUpdateDate());
			boardRepo.save(origin);
		});
		;
		return "redirect:/board/" + board.getId();
	}

	@GetMapping("/del")
	public String delGET(Long id) {
		boardRepo.deleteById(id);
		return "redirect:/";
	}
}
