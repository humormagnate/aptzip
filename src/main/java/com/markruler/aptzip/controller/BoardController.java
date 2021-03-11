package com.markruler.aptzip.controller;

import java.util.List;

import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.domain.board.BoardRequestDto;
import com.markruler.aptzip.domain.board.CategoryEntity;
import com.markruler.aptzip.domain.board.LikeEntity;
import com.markruler.aptzip.domain.board.LikeRequestDto;
import com.markruler.aptzip.domain.user.UserAccountRequestDto;
import com.markruler.aptzip.helper.CustomPage;
import com.markruler.aptzip.helper.CustomPageMaker;
import com.markruler.aptzip.service.BoardService;
import com.markruler.aptzip.service.CategoryService;
import com.markruler.aptzip.service.LikeService;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/boards")
@Controller
public class BoardController {

  private final BoardService boardService;
  private final CategoryService categoryService;
  private final LikeService likeService;

  @GetMapping("/new")
  public String goWritePage(Model model) {
    List<CategoryEntity> categories = categoryService.findAll();
    log.debug("categories: {}", categories);
    model.addAttribute("categories", categories);
    return "board/new";
  }

  @PostMapping("/new")
  public String writeNewBoard(
  // @formatter:off
    @ModelAttribute BoardRequestDto board,
    @RequestParam(value = "categoryId", defaultValue = "") String categoryId,
    @AuthenticationPrincipal UserAccountRequestDto user,
    RedirectAttributes redirectAttributes
  // @formatter:on
  ) throws Exception {
    if (board.getBoardTitle().isEmpty() || board.getBoardContent().isEmpty() || categoryId.isEmpty()) {
      log.debug("board: {}", board);
      log.debug("categoryID: {}", categoryId);
      return "";
    }

    boardService.save(board, categoryId, user);
    // Post-Redirect-Get 방식: 리다이렉트를 하지 않으면 사용자가 여러 번 게시물을 등록할 수 있기 때문에 이를 방지하기 위함
    redirectAttributes.addFlashAttribute("msg", "success");
    return "redirect:/";
  }

  @GetMapping("/{id}")
  public String read(Model model, @PathVariable("id") Long boardId,
      @AuthenticationPrincipal UserAccountRequestDto user) {
    log.debug("user: {}", user);
    boardService.findById(boardId, user, model);
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

  @Secured(value = { "ROLE_USER", "ROLE_ADMIN" })
  @GetMapping("/{id}/edit")
  public String editGet(Model model, @PathVariable("id") Long id) {
    List<CategoryEntity> categories = boardService.findByIdFromEdit(id, model);
    model.addAttribute("categories", categories);
    return "board/write";
  }

  @ResponseBody
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
    boardService.deleteById(id);
    return ResponseEntity.ok("성공적으로 삭제되었습니다.");
  }

  @ResponseBody
  @PutMapping("/{id}")
  public ResponseEntity<String> updateBoard(@RequestBody BoardRequestDto board) {
    boardService.updateById(board);
    return ResponseEntity.ok("{\"message\":\"성공적으로 수정되었습니다.\"}");
  }

  @ResponseBody
  @PostMapping("/{id}/like")
  public ResponseEntity<LikeEntity> createLike(@RequestBody LikeRequestDto like) {
    LikeEntity responseLike = likeService.save(like);
    return ResponseEntity.ok(responseLike);
  }

  @ResponseBody
  @DeleteMapping("/{id}/like")
  public ResponseEntity<String> deleteLike(@RequestBody LikeRequestDto like) {
    likeService.delete(like);
    return ResponseEntity.ok().build();
  }

}
