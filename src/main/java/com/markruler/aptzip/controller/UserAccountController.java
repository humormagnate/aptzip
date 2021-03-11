package com.markruler.aptzip.controller;

import javax.transaction.Transactional;

import com.markruler.aptzip.domain.user.UserAccountRequestDto;
import com.markruler.aptzip.domain.user.UserFollowEntity;
import com.markruler.aptzip.service.UserAccountService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@Controller
public class UserAccountController {
  private final UserAccountService userAccountService;

  /**
   * 사용자 페이지를 조회합니다.
   *
   * @param id
   * @param mv
   * @return
   */
  @GetMapping("/{id}")
  public String readUserPropertyById(@PathVariable("id") Long id, Model model) {
    userAccountService.readUserPropertyById(id, model);
    return "user/page-single-user";
  }

  /**
   * 사용자 비밀번호를 변경합니다.
   *
   * @param user
   * @param request
   * @return
   */
  @Transactional
  @PatchMapping("/{id}/pw")
  public ResponseEntity<String> updateUserPassword(@RequestBody UserAccountRequestDto user) {
    userAccountService.updatePassword(user);
    return ResponseEntity.ok("success");
  }

  /**
   * 사용자가 탈퇴하고 해당 정보의 플래그를 바꿉니다.
   */
  @Transactional
  @PatchMapping("/{id}")
  public ResponseEntity<String> disabledUser(@PathVariable("id") Long id) {
    userAccountService.disabledUser(id);
    return new ResponseEntity<>("success", HttpStatus.OK);
  }

  @Transactional
  @ResponseBody
  @PostMapping("/{id}/follow")
  public ResponseEntity<UserFollowEntity> createFollow(@PathVariable("id") Long id, @AuthenticationPrincipal UserAccountRequestDto user) {
    log.debug("user: {}", user);
    UserFollowEntity entity = userAccountService.createFollow(id, user);
    if (entity == null) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(entity);
    }
    return ResponseEntity.ok(entity);
  }

  @Transactional
  @DeleteMapping("/{id}/follow")
  public void deleteFollow(@PathVariable("id") Long id, @AuthenticationPrincipal UserAccountRequestDto user) {
    log.debug("user: {}", user);
    userAccountService.deleteFollow(id, user);
  }

}
