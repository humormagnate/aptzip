package com.example.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Board;
import com.example.persistence.BoardRepository;

import lombok.extern.java.Log;

@Controller
@RequestMapping("/board/")
@Log
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
	public void writeGET(Model model) {
		
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
		System.out.println(bid);
		br.findById(bid).ifPresent(board->model.addAttribute("b", board));
		return "/board/get";
	}
	
	@GetMapping("/edit")
	public String editGET(Long bid,Model model) {
		br.findById(bid).ifPresent(board->model.addAttribute("b", board));
		return "/board/write";
	}
	
	@PostMapping("/edit")
	public String editPOST(Board b) {
		Date now=new Date();
		b.setCreatedate(sdf.format(now));
		b.setUpdatedate(sdf.format(now));
		b.setDel_yn("N");
		System.out.println(""+b);
		br.save(b);
		return "redirect:/board/list";
	}
}
