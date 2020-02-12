package com.example.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.board.Board;
import com.example.domain.user.UserResponseDto;
import com.example.persistence.BoardRepository;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/board/")
@Slf4j
public class BoardController {

	@Autowired
	private BoardRepository br;
	
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
	
	@GetMapping("/list")
	public void list(Model model) {
		
		List<Board> list=new ArrayList<Board>();
		for (Board str : br.findAll()) {
	        list.add(str);
	    }
		model.addAttribute("list", list);
	}
	
	@GetMapping("/write")
	public ModelAndView writeGET(Principal principal, ModelAndView mv) {
		log.info("principal : " + principal);
		mv.addObject("principal", principal)
			.setViewName("/board/write");
		return mv;
	}
	
	@PostMapping("/write")
	public String writePOST(Board b) {
		Date now=new Date();
		b.setCreatedate(sdf.format(now));
		b.setUpdatedate(sdf.format(now));
		b.setDel_yn("N");
		System.out.println(""+b);
		br.save(b);
		return "redirect:/board/list";
	}
	
	@GetMapping("/{bid}")
	public String get(Model model,@PathVariable("bid") Long bid) {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		UserResponseDto user=(UserResponseDto)authentication.getPrincipal();
		br.findById(bid).ifPresent(board->{
			board.setUser_id(user.getId());
			board.setViewCount(board.getViewCount()+1);
			br.save(board);
			
			model.addAttribute("b", board);
		});
		
		return "/board/get";
	}
	
	@GetMapping("/edit")
	public String editGET(Long bid,Model model) {
		br.findById(bid).ifPresent(board->model.addAttribute("b", board));
		return "/board/write";
	}
	
	@PostMapping("/edit")
	public String editPOST(Board b,RedirectAttributes rttr) {
		Date now=new Date();
		b.setUpdatedate(sdf.format(now));
		b.setDel_yn("N");
		
		br.findById(b.getBid()).ifPresent(origin->{
			origin.setCategory(b.getCategory());
			origin.setTitle(b.getTitle());
			origin.setContent(b.getContent());
			origin.setUpdatedate(b.getUpdatedate());
			br.save(origin);
		});;
		return "redirect:/board/"+b.getBid();
	}
	
	@GetMapping("/del")
	public String delGET(Long bid) {
		br.deleteById(bid);
		return "redirect:/board/list";
	}
}
