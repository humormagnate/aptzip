package com.markruler.aptzip.controller.view;

import java.util.List;

import com.markruler.aptzip.domain.board.model.BoardEntity;
import com.markruler.aptzip.domain.board.model.BoardRequestDto;
import com.markruler.aptzip.domain.board.model.Category;
import com.markruler.aptzip.domain.board.model.CustomPage;
import com.markruler.aptzip.domain.board.model.CustomPageMaker;
import com.markruler.aptzip.domain.board.model.LikeRequestDto;
import com.markruler.aptzip.domain.board.service.BoardService;
import com.markruler.aptzip.domain.board.service.LikeService;
import com.markruler.aptzip.domain.user.model.UserAccountRequestDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardView {
  Logger log = LoggerFactory.getLogger(BoardView.class);

  private final BoardService boardService;
  private final LikeService likeService;

  @GetMapping("/new")
  public String goWritePage(Model model) {
    Category[] categories = Category.values();
    for (Category c : categories) {
      log.debug("categories: {}", c);
    }
    model.addAttribute("categories", categories);
    return "board/write";
  }

  @Secured(value = { "ROLE_USER", "ROLE_ADMIN" })
  @GetMapping("/{id}/edit")
  public String goEditPage(Model model, @PathVariable("id") Long id) {
    model.addAttribute("board", BoardRequestDto.of(boardService.findById(id)));
    return "board/write";
  }

  @GetMapping("/{id}")
  public String read(Model model, @PathVariable("id") Long boardId,
      @AuthenticationPrincipal UserAccountRequestDto user) {
    BoardEntity board = boardService.findById(boardId);
    List<LikeRequestDto> likes = likeService.findLikesByBoard(board);
    LikeRequestDto like = null;
    if (user != null) {
      for (LikeRequestDto el : likes) {
        if (el.getUser().getEmail().equals(user.getEmail())) {
          like = el;
        }
      }
    }
    // @formatter:off
    model
      .addAttribute("board", board)
      .addAttribute("likes", likes)
      .addAttribute("like", like);
    // @formatter:on
    return "board/page-single-topic";
  }

  @GetMapping("/apt/{code}")
  public String getThreadByApartmentCode(
  // @formatter:off
      @PathVariable("code") String apartmentCode,
      @AuthenticationPrincipal UserAccountRequestDto user,
      @ModelAttribute("customPage") CustomPage customPage,
      Model model
      // @formatter:on
  ) {
    log.debug("Apartment Code: {}", apartmentCode);
    Page<BoardEntity> boards = boardService.listBoardByPage(apartmentCode, customPage);
    CustomPageMaker<BoardEntity> list = new CustomPageMaker<>(boards);

    // @formatter:off
    model
      .addAttribute("principal", user)
      .addAttribute("list", list)
      .addAttribute("customPage", customPage);
    // @formatter:on

    return "apt";
  }

}
