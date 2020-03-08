package com.markruler.aptzip.controller;

import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.domain.user.UserResponseDto;
import com.markruler.aptzip.service.BoardService;
import com.markruler.aptzip.vo.PageMaker;
import com.markruler.aptzip.vo.PageVo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/apt/")  // 공통으로 id PathVariable을 줄 수 없음(ex. "/apt/{id}")
@Controller
public class AptController {

	private final BoardService boardService;

  @GetMapping("/{id}")
  public String thread(
    @PathVariable("id") Long id,
    @AuthenticationPrincipal UserResponseDto principal,
		@ModelAttribute("PageVo") PageVo pageVo,
    Model model
  ) {
		log.info("principal : {} =============================================================", principal);

		Pageable page = pageVo.makePageable(0, "id");
    log.info("page : {}", page);
    
    // apt id를 받아 해당 아파트의 thread만 받음
    pageVo.setAptId(id);
    Page<BoardEntity> boards = boardService.findBoardByDynamicQuery(page, pageVo);
		log.info("Page<BoardEntity> : {}", boards);
		log.info("TOTAL PAGE NUMBER : {}", boards.getTotalPages());

		PageMaker<BoardEntity> list = new PageMaker<BoardEntity>(boards);
    log.info("PageMaker : {}", list);
    
		int newBoard = 0;
    // List<BoardEntity> list = new ArrayList<BoardEntity>();
		// for (BoardEntity str : board) {
		// 	list.add(str);
		// 	if (new TemporalsAptzip(Locale.KOREA).isItOneHourAgo(str.getCreateDate())) {
		// 		newBoard++;
		// 	}
		// }
		
    model.addAttribute("principal", principal)
         .addAttribute("list", list)
         .addAttribute("pageVo", pageVo)
         .addAttribute("newBoard", newBoard);
		
		return "apt";
  }
}