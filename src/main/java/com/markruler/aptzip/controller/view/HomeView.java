package com.markruler.aptzip.controller.view;

import com.markruler.aptzip.domain.board.model.BoardEntity;
import com.markruler.aptzip.domain.board.model.CustomPage;
import com.markruler.aptzip.domain.board.model.CustomPageMaker;
import com.markruler.aptzip.domain.board.service.BoardService;
import com.markruler.aptzip.domain.user.model.UserAccountRequestDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeView {
  Logger log = LoggerFactory.getLogger(HomeView.class);

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

}
