package com.markruler.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.markruler.domain.board.BoardEntity;
import com.markruler.domain.common.AptEntity;
import com.markruler.domain.user.AptzipRoleEntity;
import com.markruler.domain.user.AptzipUserEntity;
import com.markruler.domain.user.UserResponseDto;
import com.markruler.persistence.board.BoardRepository;
import com.markruler.persistence.user.UserJpaRepository;
import com.markruler.service.BoardService;
import com.markruler.vo.PageMaker;
import com.markruler.vo.PageVo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CommonController {
	
	private final UserJpaRepository userRepository;
	private final BoardRepository boardRepository;
	private final BoardService boardService;

	@GetMapping
	public ModelAndView home(
		@AuthenticationPrincipal UserResponseDto principal,
		@ModelAttribute("PageVo") PageVo pageVo,
		ModelAndView mv
	) {
		log.info("principal : {} =============================================================", principal);

		Pageable page = pageVo.makePageable(0, "id");
		log.info("page : {}", page);

		Page<BoardEntity> boards = boardService.findBoardByDynamicQuery(page, pageVo);
		log.info("Page<BoardEntity> : {}", boards);
		log.info("TOTAL PAGE NUMBER : {}", boards.getTotalPages());

		PageMaker<BoardEntity> list = new PageMaker<BoardEntity>(boards);
		log.info("PageMaker : {}", list);
		
		int newBoard = 0;
		
		mv.addObject("principal", principal)
			.addObject("list", list)
			.addObject("pageVo", pageVo)
			.addObject("newBoard", newBoard)
			.setViewName("index");
		
		return mv;
	}
	
	@GetMapping("/zip")
	public void zip(@AuthenticationPrincipal UserResponseDto principal, Model model) {
		
		Long aptId = principal.getApt().getId();
		AptEntity apt = AptEntity.builder().id(aptId).build();

		List<AptzipUserEntity> admins = userRepository.findAllByAptAndRole(apt, new AptzipRoleEntity("ADMIN"));
		log.info("admins : {}", admins);

		List<BoardEntity> boards =
		StreamSupport.stream(boardRepository.findAllByApt(apt).spliterator(), false)
								 .collect(Collectors.toList());
		
		model.addAttribute("admins", admins)
				 .addAttribute("boardSize", boards.size());
	}

	@GetMapping("/categories")
	public void categories() {}
}
