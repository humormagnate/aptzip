package com.markruler.aptzip.controller.api;

import java.util.Optional;

import com.markruler.aptzip.domain.board.model.BoardRequestDto;
import com.markruler.aptzip.domain.board.model.LikeEntity;
import com.markruler.aptzip.domain.board.model.LikeRequestDto;
import com.markruler.aptzip.domain.board.service.BoardService;
import com.markruler.aptzip.domain.board.service.LikeService;
import com.markruler.aptzip.domain.user.model.UserAccountEntity;
import com.markruler.aptzip.domain.user.model.UserAccountRequestDto;
import com.markruler.aptzip.domain.user.service.UserAccountService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/boards")
@Api(tags = "boards")
@RequiredArgsConstructor
public class BoardApi {
  Logger log = LoggerFactory.getLogger(BoardApi.class);

  private final BoardService boardService;
  private final UserAccountService userService;
  private final LikeService likeService;

  @PostMapping("/new")
  public String writeNewBoard(
  // @formatter:off
    @ModelAttribute BoardRequestDto board,
    @RequestParam(value = "category", defaultValue = "COMMON") String category,
    @AuthenticationPrincipal UserAccountRequestDto user,
    RedirectAttributes redirectAttributes
  // @formatter:on
  ) throws Exception {
    log.debug("board: {}", board);
    log.debug("category: {}", category);
    log.debug("user: {}", user);

    if (board.getTitle() == null || board.getTitle().isEmpty() || board.getContent() == null
        || board.getContent().isEmpty() || category == null || category.isEmpty() || user == null) {
      return "redirect:/error";
    }
    Optional<UserAccountEntity> userEntity = userService.findByEmailIgnoreCase(user.getEmail());
    if (userEntity.isEmpty()) {
      return "redirect:/error";
    }
    boardService.save(board, category, userEntity.get());
    // Post-Redirect-Get 방식: 리다이렉트를 하지 않으면 사용자가 여러 번 게시물을 등록할 수 있기 때문에 이를 방지하기 위함
    redirectAttributes.addFlashAttribute("msg", "success");
    return "redirect:/";
  }

  @PutMapping("/{id}")
  public ResponseEntity<String> updateBoard(@RequestBody BoardRequestDto board) {
    boardService.updateById(board);
    return ResponseEntity.ok("{\"message\":\"성공적으로 수정되었습니다.\"}");
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
    boardService.deleteById(id);
    return ResponseEntity.ok("성공적으로 삭제되었습니다.");
  }

  @PostMapping("/{id}/like")
  public ResponseEntity<LikeEntity> createLike(@RequestBody LikeRequestDto like) {
    LikeEntity responseLike = likeService.save(like);
    return ResponseEntity.ok(responseLike);
  }

  @DeleteMapping("/{id}/like")
  public ResponseEntity<String> deleteLike(@RequestBody LikeRequestDto like) {
    likeService.delete(like);
    return ResponseEntity.ok().build();
  }

}
