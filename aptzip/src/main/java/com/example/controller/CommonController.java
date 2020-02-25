package com.example.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.example.config.thymeleaf.expression.TemporalsAptzip;
import com.example.domain.board.BoardEntity;
import com.example.domain.common.AptEntity;
import com.example.domain.user.AptzipRoleEntity;
import com.example.domain.user.AptzipUserEntity;
import com.example.domain.user.UserRequestDto;
import com.example.domain.user.UserResponseDto;
import com.example.persistence.BoardRepository;
import com.example.persistence.UserJpaRepository;
import com.example.service.UserService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CommonController {
	
	private final BoardRepository boardRepo;
	private final UserJpaRepository userRepo;
	private final UserService userService;

	// @Secured({ "ROLE_ADMIN" })
	@GetMapping("/")
  //public ModelAndView home(Principal principal, ModelAndView mv) {
	public ModelAndView home(@AuthenticationPrincipal UserResponseDto principal, ModelAndView mv
					) {
						// @PageableDefault(direction = Sort.Direction.DESC,
						// 							 sort="id",
						// 							 size = 10,
						// 							 page = 0) Pageable page
		// https://itstory.tk/entry/Spring-Security-%ED%98%84%EC%9E%AC-%EB%A1%9C%EA%B7%B8%EC%9D%B8%ED%95%9C-%EC%82%AC%EC%9A%A9%EC%9E%90-%EC%A0%95%EB%B3%B4-%EA%B0%80%EC%A0%B8%EC%98%A4%EA%B8%B0
		// https://lemontia.tistory.com/602
		log.info(principal + "=========================================================");

		List<BoardEntity> list = new ArrayList<BoardEntity>();
		Iterable<BoardEntity> board = boardRepo.findAllByOrderByIdDesc();
		int newBoard = 0;

		for (BoardEntity str : board) {
			list.add(str);
			if (new TemporalsAptzip(Locale.KOREA).isItOneHourAgo(str.getCreateDate())) {
				newBoard++;
			}
		}
		// log.info("page : " + page.toString());
		// log.info("list.size() : " + list.size());
		// log.info("list : " + list.toString());
		
		mv.addObject("principal", principal)
			.addObject("list", list)
			.addObject("newBoard", newBoard)
			.setViewName("index");
		
		return mv;
	}
	
	@GetMapping("/login")
	public void login() {}
	
	@GetMapping("/join")
	public void signup() {}
	
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
			log.info("userForm : {}", userForm);
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

	@GetMapping("/zip")
	public void zip(@AuthenticationPrincipal UserResponseDto principal, Model model) {
		
		Long aptId = principal.getApt().getId();
		AptEntity apt = AptEntity.builder().id(aptId).build();

		List<AptzipUserEntity> admins =
			userRepo.findAllByAptAndRole(apt,
																	 new AptzipRoleEntity("ADMIN"));
		log.info("admins : {}", admins);

		// https://www.baeldung.com/java-iterable-to-collection
		List<BoardEntity> boards =
		StreamSupport.stream(boardRepo.findAllByApt(apt).spliterator(), false)
		.collect(Collectors.toList());
		
		// List<CommentEntity> comments =
		// 	StreamSupport.stream(commentRepo.findAllByApt(apt).spliterator(), false)
		// 							 .collect(Collectors.toList());
		
		model.addAttribute("admins", admins)
				//  .addAttribute("commentSize", comments.size())
				 .addAttribute("boardSize", boards.size());
	}

	@GetMapping("/categories")
	public void categories() {}
}
