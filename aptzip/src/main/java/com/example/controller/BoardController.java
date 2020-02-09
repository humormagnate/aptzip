package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Board;
import com.example.persistence.BoardRepository;

import lombok.extern.java.Log;

@Controller
@RequestMapping("/board/")
//@Log
public class BoardController {

	@Autowired
	private BoardRepository br;
	
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
	public void writePOST(Model model) {
	}
}
