package com.markruler.aptzip.controller;

import java.util.List;

import javax.transaction.Transactional;

import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.domain.board.CommentEntity;
import com.markruler.aptzip.domain.user.AptzipUserEntity;
import com.markruler.aptzip.domain.user.UserFollowEntity;
import com.markruler.aptzip.domain.user.UserRequestDto;
import com.markruler.aptzip.domain.user.UserResponseDto;
import com.markruler.aptzip.persistence.board.BoardRepository;
import com.markruler.aptzip.persistence.board.CommentRepository;
import com.markruler.aptzip.persistence.user.FollowRepository;
import com.markruler.aptzip.service.UserAccountService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@RequestMapping("/user")
@Controller
public class UserAccountController {

  private final BoardRepository boardRepo;
  private final UserAccountService userService;
  private final FollowRepository followRepo;
  // private final FollowQueryRepository followQuery;
  private final CommentRepository commentRepo;
  private final PasswordEncoder passwordEncoder;

  /**
   * 사용자 페이지를 조회합니다.
   *
   * @param id
   * @param mv
   * @return
   */
  @GetMapping("/{id}")
  public String getUserID(@PathVariable("id") Long id, Model model) {
    List<BoardEntity> boards = boardRepo.findByUserIdOrderByIdDesc(id);
    List<CommentEntity> comments = commentRepo.findByUserIdOrderByIdDesc(id);
    List<UserFollowEntity> followings = followRepo.findAllByFollowing(id);
    List<UserFollowEntity> followers = followRepo.findAllByFollower(id);

    AptzipUserEntity user = userService.findById(id);

    model.addAttribute("boards", boards).addAttribute("comments", comments)
        .addAttribute("infouser", user).addAttribute("followings", followings)
        .addAttribute("followers", followers);
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
  public ResponseEntity<String> updateUserPassword(@RequestBody UserRequestDto user) {
    log.debug("user : " + user);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userService.updatePassword(user);
    return ResponseEntity.ok("success");
  }

  /**
   * 사용자가 탈퇴하고 해당 정보의 플래그를 바꿉니다.
   */
  @Transactional
  @PatchMapping("/{id}")
  public ResponseEntity<String> disabledUser(@PathVariable("id") Long id) {
    userService.disabledUser(id);
    return new ResponseEntity<>("success", HttpStatus.OK);
  }

  // TODO post와 delete 나누기
  @Transactional
  @ResponseBody
  @PostMapping("/{id}/follow")
  public String insertFollow(@PathVariable("id") Long id,
      @AuthenticationPrincipal UserResponseDto principal) {

    AptzipUserEntity following = AptzipUserEntity.builder().id(id).build();
    AptzipUserEntity follower = principal.toEntity();
    UserFollowEntity relationship = followRepo.findByFollowingAndFollower(following, follower);

    if (relationship != null) {
      followRepo.delete(relationship);
      return "delete";
    } else {
      followRepo.save(UserFollowEntity.builder().following(following).follower(follower).build());
    }
    return "save";
  }

}
