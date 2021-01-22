package com.markruler.aptzip.controller;

import java.util.List;
import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.domain.board.CategoryEntity;
import com.markruler.aptzip.domain.user.UserResponseDto;
import com.markruler.aptzip.service.BoardService;
import com.markruler.aptzip.service.CategoryService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board/")
@Controller
public class BoardController {

  private final BoardService boardService;
  private final CategoryService categoryService;
  // private final LikeService likeService;
  // private final SimpMessageSendingOperations smso;

  @GetMapping("/write")
  public void goWritePage(Model model) {
		List<CategoryEntity> categories = categoryService.findAll();
    model.addAttribute("categories", categories);
  }

  @PostMapping("/write")
  public String postWrite(
  // @formatter:off
    // TODO: Replace this persistent entity with a simple POJO or DTO object.sonarlint(java:S4684)
    BoardEntity board,
    String categoryID,
    @AuthenticationPrincipal UserResponseDto principal,
    RedirectAttributes rttr
  // @formatter:on
  ) throws Exception {

    if (board.getBoardTitle().isEmpty() || categoryID.isEmpty()) {
      return "";
    }

    boardService.save(board, categoryID, principal);
    // Post-Redirect-Get 방식: 리다이렉트를 하지 않으면 사용자가 여러 번 게시물을 등록할 수 있기 때문에 이를 방지하기 위함
    rttr.addFlashAttribute("msg", "success");
    return "redirect:/";
  }

  @GetMapping("/{id}")
  public String read(
  // @formatter:off
    Model model,
    @PathVariable("id") Long id,
    @AuthenticationPrincipal UserResponseDto principal
  // @formatter:on
  ) {
    boardService.findById(id, principal, model);
    return "board/page-single-topic";
  }

  @ResponseBody
  @DeleteMapping("/{id}")
  public String deleteById(@PathVariable("id") Long id) {
    boardService.deleteById(id);
    return "성공적으로 삭제되었습니다.";
  }

  @ResponseBody
  @PutMapping("/{id}")
  public String put(
  // @formatter:off
    @PathVariable("id") Long id,
    // TODO: Replace this persistent entity with a simple POJO or DTO object.sonarlint(java:S4684)
    @ModelAttribute BoardEntity board,
    String categoryId
  // @formatter:on
  ) {
    if (categoryId.isEmpty()) {
      return "카테고리는 필수사항입니다.";
    }
    boardService.updateById(id, board, categoryId);
    return "성공적으로 수정되었습니다.";
  }

  @Secured(value = {"ROLE_USER", "ROLE_ADMIN"})
  @GetMapping("/{id}/edit")
  public String editGet(Model model, @PathVariable("id") Long id) {
    List<CategoryEntity> categories = boardService.findByIdFromEdit(id, model);
    model.addAttribute("categories", categories);
    return "board/write";
  }

  /**
   * 클라이언트는 @MessageMapping 으로 request 서버는 @SendTo 로 response
   */
  @MessageMapping("/nbax") // 전역 RequestMapping(여기서 '/board')에 적용되지 않는다.
                           // -> 따로 컨트롤러를 두자(MessageController)
  @SendTo("/topic/messagexx") // publishing
  public String newBoardAlertx(String message) throws Exception {
    log.debug("STOMP >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + message);
    return message;
  }

  @MessageMapping("/nbaxx")
  @SendTo("/topic/messagexx") // publishing
  public String newBoardAlertxx(String message) throws Exception {
    log.debug("STOMP >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + message);
    return message;
  }

}
