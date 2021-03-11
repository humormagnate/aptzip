package com.markruler.aptzip.controller;

import java.util.List;

import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.domain.user.UserAccountEntity;
import com.markruler.aptzip.domain.user.UserAccountRequestDto;
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
import lombok.extern.slf4j.Slf4j;

// TODO: Common이라는 명칭 지양, `/zip` 분리
@Slf4j
@Controller
@RequiredArgsConstructor
public class CommonController {

  private final UserAccountService userAccountService;
  private final BoardService boardService;

  @GetMapping(path = { "/", "/index/{pageNumber}" })
  public ModelAndView home(@AuthenticationPrincipal UserAccountRequestDto user,
      @PathVariable(name = "pageNumber", required = false) Integer pageNumber, ModelAndView mv) {

    if (pageNumber == null) {
      pageNumber = 1;
    }
    CustomPage customPage = new CustomPage();
    customPage.setPage(pageNumber);

    Page<BoardEntity> boards = boardService.listBoardByPage(null, customPage);
    CustomPageMaker<BoardEntity> list = new CustomPageMaker<>(boards);

    log.debug("User on Landing Page: {}", user);

    // @formatter:off
    mv.addObject("principal", user)
      .addObject("list", list)
      .setViewName("index");
    // @formatter:on

    return mv;
  }

  @GetMapping("/zip")
  public void zip(@AuthenticationPrincipal UserAccountRequestDto user, Model model) {
    log.debug("user: {}", user);
    List<UserAccountEntity> admins = userAccountService.listAdminsByApt(user);
    List<BoardEntity> boards = boardService.listBoardsByApt(user);

    // @formatter:off
    model
      .addAttribute("admins", admins)
      .addAttribute("boardSize", boards.size());
    // @formatter:on
  }

}
