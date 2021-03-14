package com.markruler.aptzip.controller.view;

import java.util.List;

import com.markruler.aptzip.domain.board.model.BoardEntity;
import com.markruler.aptzip.domain.board.service.BoardService;
import com.markruler.aptzip.domain.user.model.UserAccountEntity;
import com.markruler.aptzip.domain.user.model.UserAccountRequestDto;
import com.markruler.aptzip.domain.user.service.UserAccountService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ZipView {
  Logger log = LoggerFactory.getLogger(ZipView.class);

  private final UserAccountService userAccountService;
  private final BoardService boardService;

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
