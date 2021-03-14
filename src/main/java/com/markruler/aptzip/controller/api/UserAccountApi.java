package com.markruler.aptzip.controller.api;

import com.markruler.aptzip.domain.user.model.UserAccountRequestDto;
import com.markruler.aptzip.domain.user.model.UserFollowEntity;
import com.markruler.aptzip.domain.user.service.UserAccountService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@Api(tags = "users")
@RequiredArgsConstructor
public class UserAccountApi {
  Logger log = LoggerFactory.getLogger(UserAccountApi.class);

  private final UserAccountService userAccountService;

  @PatchMapping("/{id}/pw")
  public ResponseEntity<String> updateUserPassword(@RequestBody UserAccountRequestDto user) {
    userAccountService.updatePassword(user);
    return ResponseEntity.ok("success");
  }

  @PatchMapping("/{id}")
  public ResponseEntity<String> disabledUser(@PathVariable("id") Long id) {
    userAccountService.disabledUser(id);
    return ResponseEntity.ok("success");
  }

  @PostMapping("/{id}/follow")
  public ResponseEntity<UserFollowEntity> createFollow(@PathVariable("id") Long id,
      @AuthenticationPrincipal UserAccountRequestDto user) {
    log.debug("user: {}", user);
    UserFollowEntity entity = userAccountService.createFollow(id, user);
    if (entity == null) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(entity);
    }
    return ResponseEntity.ok(entity);
  }

  @DeleteMapping("/{id}/follow")
  public void deleteFollow(@PathVariable("id") Long id, @AuthenticationPrincipal UserAccountRequestDto user) {
    log.debug("user: {}", user);
    userAccountService.deleteFollow(id, user);
  }

}
