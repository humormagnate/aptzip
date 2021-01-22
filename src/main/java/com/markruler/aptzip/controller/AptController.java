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

// @lombok.extern.slf4j.Slf4j
@RequiredArgsConstructor
@RequestMapping("/apt/")
@Controller
public class AptController {
  private final BoardService boardService;

  @GetMapping("/{id}")
  public String thread(
  // @formatter:off
    @PathVariable("id") Long id,
    @AuthenticationPrincipal UserResponseDto principal,
    @ModelAttribute("PageVo") PageVo pageVo,
    Model model
    // @formatter:on
  ) {
    Page<BoardEntity> boards = boardService.listBoardByPage(id, pageVo);
    PageMaker<BoardEntity> list = new PageMaker<BoardEntity>(boards);

		int newBoard = 0;
    // List<BoardEntity> list = new ArrayList<BoardEntity>();
		// for (BoardEntity str : board) {
		// 	list.add(str);
		// 	if (new TemporalsAptzip(Locale.KOREA).isItOneHourAgo(str.getCreateDate())) {
		// 		newBoard++;
		// 	}
    // }

    // @formatter:off
    model.addAttribute("principal", principal)
         .addAttribute("list", list)
         .addAttribute("pageVo", pageVo)
         .addAttribute("newBoard", newBoard);

		return "apt";
  }
}
