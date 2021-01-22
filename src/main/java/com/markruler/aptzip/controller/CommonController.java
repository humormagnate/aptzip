package com.markruler.aptzip.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.domain.common.AptEntity;
import com.markruler.aptzip.domain.user.AptzipRoleEntity;
import com.markruler.aptzip.domain.user.AptzipUserEntity;
import com.markruler.aptzip.domain.user.UserResponseDto;
import com.markruler.aptzip.persistence.board.BoardRepository;
import com.markruler.aptzip.persistence.user.UserJpaRepository;
import com.markruler.aptzip.service.BoardService;
import com.markruler.aptzip.service.UserAccountService;
import com.markruler.aptzip.vo.PageMaker;
import com.markruler.aptzip.vo.PageVo;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import lombok.RequiredArgsConstructor;

// @lombok.extern.slf4j.Slf4j
@Controller
@RequiredArgsConstructor
public class CommonController {

  private final UserAccountService userAccountService;
  private final BoardService boardService;

  @GetMapping
  public ModelAndView home(
  // @formatter:off
    @AuthenticationPrincipal UserResponseDto principal,
    @ModelAttribute("PageVo") PageVo pageVo,
    ModelAndView mv
  // @formatter:on
  ) {
    Page<BoardEntity> boards = boardService.listBoardByPage(0L, pageVo);
    PageMaker<BoardEntity> list = new PageMaker<>(boards);

    int newBoard = 0;
    // TODO: 최근 게시물 개수 구하기
    // List<BoardEntity> list = new ArrayList<BoardEntity>();
    // for (BoardEntity str : board) {
    // list.add(str);
    // if (new TemporalsAptzip(Locale.KOREA).isItOneHourAgo(str.getCreateDate())) {
    // newBoard++;
    // }
    // }

    // @formatter:off
    mv.addObject("principal", principal)
      .addObject("list", list)
      .addObject("pageVo", pageVo)
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
