package com.markruler.aptzip.controller;

import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.domain.user.UserResponseDto;
import com.markruler.aptzip.helper.CustomPage;
import com.markruler.aptzip.helper.CustomPageMaker;
import com.markruler.aptzip.service.BoardService;
import org.springframework.data.domain.Page;
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

  @GetMapping("/{code}")
  public String thread(
  // @formatter:off
    @PathVariable("code") String apartmentCode,
    @AuthenticationPrincipal UserResponseDto principal,
    @ModelAttribute("customPage") CustomPage customPage,
    Model model
    // @formatter:on
  ) {
    // TODO: set apartmentCode
    Page<BoardEntity> boards = boardService.listBoardByPage(null, customPage);
    CustomPageMaker<BoardEntity> list = new CustomPageMaker<BoardEntity>(boards);

		int newBoard = 0;
    // List<BoardEntity> list = new ArrayList<BoardEntity>();
		// for (BoardEntity str : board) {
		// 	list.add(str);
		// 	if (new TemporalsAptzip(Locale.KOREA).isItOneHourAgo(str.getCreateDate())) {
		// 		newBoard++;
		// 	}
    // }

    // @formatter:off
    model
      .addAttribute("principal", principal)
      .addAttribute("list", list)
      .addAttribute("customPage", customPage)
      .addAttribute("newBoard", newBoard);
    // @formatter:on

		return "apt";
  }
}
