package com.example.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import com.example.domain.board.BoardEntity;
import com.example.domain.board.CommentEntity;
import com.example.domain.common.AptEntity;
import com.example.domain.user.AptzipUserEntity;
import com.example.domain.user.UserFollowEntity;
import com.example.domain.user.UserRequestDto;
import com.example.domain.user.UserResponseDto;
import com.example.persistence.BoardRepository;
import com.example.persistence.CommentRepository;
import com.example.persistence.FollowRepository;
import com.example.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class UserController {
	
	private final UserService userService;
	private final BoardRepository boardRepo;
	private final FollowRepository followRepo;
	// private final FollowQueryRepository followQuery;
	private final CommentRepository commentRepo;
	
	/**
	 * create
	 * @param userForm
	 * @param redirectAttributes
	 * @return
	 */
	@PostMapping(value = "/signup")
	public String insertUser(@ModelAttribute UserRequestDto userForm, RedirectAttributes redirectAttributes, String aptId) {
		log.info("=============================SIGN UP================================");
		// @Valid -> 400 error 페이지로 이동중 SecurityContext 에서 계속 405 error로 바뀐다. Why?
		try {
			Long apt = Long.valueOf(aptId);
			userForm.setApt(AptEntity.builder().id(apt).build());
			userService.save(userForm);
    // } catch (DataIntegrityViolationException e) {
    } catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			redirectAttributes.addFlashAttribute("error", true);
			return "redirect:/join";
    }
		redirectAttributes.addFlashAttribute("success", true);
		return "redirect:/login";
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	@GetMapping("/info")
	public ModelAndView deprecatedInfo(@AuthenticationPrincipal UserResponseDto principal, ModelAndView mv) {
		// Principal principal = request.getUserPrincipal();
		List<BoardEntity> list = boardRepo.findByUserIdOrderByIdDesc(principal.getId());
		log.info(list.toString());
		
		mv.addObject("principal", principal)
			.addObject("list", list)
			.setViewName("user/page-single-user");
		return mv;
	}

	/**
	 * retrieve
	 * @param id
	 * @param mv
	 * @return
	 */
	@GetMapping("/{id}/info")
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
	@Transactional
	@PutMapping("/edit")
	public ResponseEntity<List<String>> updateUser(@RequestBody UserRequestDto user, HttpServletRequest request) {
		log.info("user : " + user);
		log.info("request : " + request);

		// request.getSession().setAttribute("msg", "비번변경");
		// ServerRequest request -> java.lang.NoSuchMethodException: org.springframework.web.servlet.function.ServerRequest.<init>()
		// request.session().setAttribute("msg", "비밀번호가 변경되었습니다. 다시 로그인해주세요.");

		return new ResponseEntity<>(Arrays.asList("success"), HttpStatus.OK);
	}

	/**
	 * delete
	 */
	@Transactional
	@DeleteMapping
	public void deleteUser() {

	}

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

	@Transactional
	@DeleteMapping("/{id}/leave")
	public void deleteFollow() {

	}

}
