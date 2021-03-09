package com.markruler.aptzip.controller;

import java.util.List;
import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.domain.user.AptzipUserEntity;
import com.markruler.aptzip.domain.user.UserResponseDto;
import com.markruler.aptzip.helper.CustomPage;
import com.markruler.aptzip.helper.CustomPageMaker;
import com.markruler.aptzip.service.BoardService;
import com.markruler.aptzip.service.UserAccountService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import lombok.RequiredArgsConstructor;

// @lombok.extern.slf4j.Slf4j
@Controller
@RequiredArgsConstructor
public class CommonController {

  private final UserAccountService userAccountService;
  private final BoardService boardService;

  @GetMapping(path = {"/", "/index/{pageNumber}"})
  public ModelAndView home(
  // @formatter:off
    @AuthenticationPrincipal UserResponseDto principal,
    @PathVariable(name = "pageNumber", required = false) Integer pageNumber,
    ModelAndView mv
  // @formatter:on
  ) {
    if (pageNumber == null) pageNumber = 1;
    CustomPage customPage = new CustomPage();
    customPage.setPage(pageNumber);
    Page<BoardEntity> boards = boardService.listBoardByPage(null, customPage);
    CustomPageMaker<BoardEntity> list = new CustomPageMaker<>(boards);

    // TODO: 최근 게시물 개수 구하기
    int newBoard = 0;
    // List<BoardEntity> list = new ArrayList<BoardEntity>();
    // for (BoardEntity str : board) {
    // list.add(str);
    // if (new TemporalsAptzip(Locale.KOREA).isLessThanOneHour(str.getCreateDate())) {
    // newBoard++;
    // }
    // }

    // @formatter:off
    mv.addObject("principal", principal)
      .addObject("list", list)
      .addObject("newBoard", newBoard)
      .setViewName("index");
    // @formatter:on

    return mv;
  }

  @GetMapping("/zip")
  public void zip(@AuthenticationPrincipal UserResponseDto principal, Model model) {
    List<AptzipUserEntity> admins = userAccountService.listAdminsByAPT(principal);
    List<BoardEntity> boards = boardService.listBoardsByAPT(principal);

    // @formatter:off
    model
      .addAttribute("admins", admins)
      .addAttribute("boardSize", boards.size());
    // @formatter:on
  }

  // @GetMapping("/categories")
  // public void categories() {
  // }
}
