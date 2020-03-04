package com.markruler.controller;

import java.util.List;

import javax.transaction.Transactional;

import com.markruler.domain.board.BoardEntity;
import com.markruler.domain.board.CommentEntity;
import com.markruler.domain.user.AptzipUserEntity;
import com.markruler.domain.user.UserFollowEntity;
import com.markruler.domain.user.UserRequestDto;
import com.markruler.domain.user.UserResponseDto;
import com.markruler.persistence.board.BoardRepository;
import com.markruler.persistence.board.CommentRepository;
import com.markruler.persistence.user.FollowRepository;
import com.markruler.service.UserAccountService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	 * retrieve
	 * @param id
	 * @param mv
	 * @return
	 */
	@GetMapping("/{id}")
	public String info(@PathVariable("id") Long id, Model model) {
		List<BoardEntity> boards = boardRepo.findByUserIdOrderByIdDesc(id);
		List<CommentEntity> comments = commentRepo.findByUserIdOrderByIdDesc(id);
		// List<AptzipUserEntity> following = followQuery.following(id, 10L, 0L);
		List<UserFollowEntity> followings = followRepo.findAllByFollowing(id);
		List<UserFollowEntity> followers = followRepo.findAllByFollower(id);
		// List<UserFollowEntity> following = followRepo.findAllByFrom(AptzipUserEntity.builder().id(id).build());
		// List<UserFollowEntity> follower = followRepo.findAllByTo(AptzipUserEntity.builder().id(id).build());

		AptzipUserEntity user = userService.findById(id);
		log.info("boards : {}", boards.toString());
		log.info("comments : {}", comments.toString());
		log.info("infouser : {}", user);
		log.info("followings : {}", followings);
		// log.info("followers : {}", followers);
		
		model.addAttribute("boards", boards)
				 .addAttribute("comments", comments)
				 .addAttribute("infouser", user)
				 .addAttribute("followings", followings)
				 .addAttribute("followers", followers);
		return "user/page-single-user";
	}
	
	/**
	 * update
	 * @param user
	 * @param request
	 * @return
	 */
	// @ResponseBody // -> 415 error
	// @PreAuthorize("#updateUser.email == authentication.name")
	// https://www.baeldung.com/http-put-patch-difference-spring
	// PATCH method
	@Transactional
	@PatchMapping("/{id}/pw")
	public ResponseEntity<String> updateUserPassword(@RequestBody UserRequestDto user) {
		log.info("user : " + user);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
		userService.updatePassword(user);
		return ResponseEntity.ok("success");
	}
	
	/**
	 * delete
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
	public String insertFollow(@PathVariable("id") Long id, @AuthenticationPrincipal UserResponseDto principal) {
		log.info("target id : {}", id);
		log.info("login user : {}", principal.toString());
		
		AptzipUserEntity following = AptzipUserEntity.builder().id(id).build();
		AptzipUserEntity follower = principal.toEntity();
		UserFollowEntity relationship = followRepo.findByFollowingAndFollower(following, follower);

		if (relationship != null) {
			followRepo.delete(relationship);
			return "delete";
		} else {
			followRepo.save(UserFollowEntity.builder()
																			.following(following)
																			.follower(follower)
																			.build());
		}

		return "save";
	}

}
