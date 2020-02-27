package com.example.controller;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.example.config.thymeleaf.expression.TemporalsAptzip;
import com.example.domain.board.BoardEntity;
import com.example.domain.common.AptEntity;
import com.example.domain.user.AptzipRoleEntity;
import com.example.domain.user.AptzipUserEntity;
import com.example.domain.user.UserResponseDto;
import com.example.persistence.board.BoardRepository;
import com.example.persistence.user.UserJpaRepository;
import com.example.vo.PageMaker;
import com.example.vo.PageVo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CommonController {
	
	private final BoardRepository boardRepoistory;
	private final UserJpaRepository userRepository;

	// @Secured({ "ROLE_ADMIN" })
	// @GetMapping("/")	// /aptzip?page=3 -> /aptzip/?page=3
	@GetMapping	// /aptzip?page=3 -> /aptzip/?page=3
  //public ModelAndView home(Principal principal, ModelAndView mv) {
	public ModelAndView home(
		@AuthenticationPrincipal UserResponseDto principal,
		@ModelAttribute("PageVo") PageVo pageVo,
		ModelAndView mv
	) {
		// @PageableDefault(
		// 	direction = Sort.Direction.DESC,
		// 	sort = "id",
		// 	size = 10,
		// 	page = 0) Pageable page) {

		// https://itstory.tk/entry/Spring-Security-%ED%98%84%EC%9E%AC-%EB%A1%9C%EA%B7%B8%EC%9D%B8%ED%95%9C-%EC%82%AC%EC%9A%A9%EC%9E%90-%EC%A0%95%EB%B3%B4-%EA%B0%80%EC%A0%B8%EC%98%A4%EA%B8%B0
		// https://lemontia.tistory.com/602
		log.info("principal : {} =============================================================", principal);

		Pageable page = pageVo.makePageable(0, "id");
		log.info("page : {}", page);

		// Iterable<BoardEntity> board = boardRepoistory.findAllByOrderByIdDesc();
		// QuerydslJpaRepository<T>
		Page<BoardEntity> boards = boardRepoistory.findAll(boardRepoistory.makePredicate(pageVo.getType(), pageVo.getKeyword()), page);
		log.info("Page<BoardEntity> : {}", boards);
		
		log.info("TOTAL PAGE NUMBER : {}", boards.getTotalPages());

		// 작성한지 한 시간이 안 된 게시글들 카운트
		// List<BoardEntity> list = new ArrayList<BoardEntity>();
		int newBoard = 0;
		for (BoardEntity board : boards) {
			// list.add(board);
			if (new TemporalsAptzip(Locale.KOREA).isItOneHourAgo(board.getCreateDate())) {
				newBoard++;
			}
			log.info("board list : {}", board);
		}
		// log.info("page : " + page.toString());
		// log.info("list.size() : " + list.size());
		// log.info("list : " + list.toString());
		PageMaker<BoardEntity> list = new PageMaker<BoardEntity>(boards);
		log.info("PageMaker : {}", list);
		
		mv.addObject("principal", principal)
			.addObject("list", list)
			.addObject("pageVo", pageVo)
			.addObject("newBoard", newBoard)
			.setViewName("index");
		
		return mv;
	}
	
	@GetMapping("/zip")
	public void zip(@AuthenticationPrincipal UserResponseDto principal, Model model) {
		
		Long aptId = principal.getApt().getId();
		AptEntity apt = AptEntity.builder().id(aptId).build();

		List<AptzipUserEntity> admins =
			userRepository.findAllByAptAndRole(apt,
																	 new AptzipRoleEntity("ADMIN"));
		log.info("admins : {}", admins);

		// https://www.baeldung.com/java-iterable-to-collection
		List<BoardEntity> boards =
		StreamSupport.stream(boardRepoistory.findAllByApt(apt).spliterator(), false)
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
