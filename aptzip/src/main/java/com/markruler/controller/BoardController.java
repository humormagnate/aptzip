package com.markruler.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.markruler.domain.board.BoardEntity;
import com.markruler.domain.board.CategoryEntity;
import com.markruler.domain.board.LikeEntity;
import com.markruler.domain.user.UserResponseDto;
import com.markruler.persistence.board.BoardRepository;
import com.markruler.persistence.board.CategoryRepository;
import com.markruler.persistence.board.LikeRepository;

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

	private final BoardRepository boardRepo;
	private final CategoryRepository categoryRepo;
	private final LikeRepository likeRepo;
	// private final SimpMessageSendingOperations smso;

	@GetMapping("/write")
	public void writeGet(Model model) {
		List<CategoryEntity> categories =
			StreamSupport.stream(categoryRepo.findAll().spliterator(), false)
    							 .collect(Collectors.toList());
		model.addAttribute("categories", categories);
	}

	// AuthenticationPrincipal은 Context에 있기 때문에 계속 보내줄 필요 없음
	// public ModelAndView writeGet(@AuthenticationPrincipal UserResponseDto principal, ModelAndView mv) {
	// 	mv.addObject("principal", principal)
	// 		.setViewName("board/page-create-topic");
	// 	return mv;
	// }
	
	// UserDetails를 구현한 UserRequestDto는 @AuthenticationPrincipal 이 안먹히고
	// UserDetails를 구현한 User를 상속한 UserResponseDto는 먹힌다. Why?
	@PostMapping("/write")
	public String writePost(BoardEntity board, @AuthenticationPrincipal UserResponseDto principal, RedirectAttributes rttr, String categoryName)
		throws Exception {
		
		log.info("/board/write/post////////////////////////////////////////////////////////////////////////////");
		log.info(board.toString());
		log.info(principal.toString());
		
		if ("".equals(board.getBoardTitle())) {
			return "";
		}

		if ("".equals(categoryName) && categoryName == null) {
			return "";
		}

		// categoryName 대신 번호로 받기
		// 글쓸 때마다 db를 2번 감
		categoryRepo.findByName(categoryName).ifPresent(category -> {
			board.setCategory(category);
			board.setBoardStatus("Y");
			board.setUser(principal.toEntity());
			board.setApt(board.getUser().getApt());
			board.setUpdateDate(LocalDateTime.now());

			log.info(board.toString());
			log.info(board.getUser().getApt().toString());
	
			boardRepo.save(board);
		});

		/*
			Post-Redirect-Get 방식
			: 리다이렉트를 하지 않으면 사용자가 여러 번 게시물을 등록할 수 있기 때문에 이를 방지하기 위함
		*/
		rttr.addFlashAttribute("msg", "success");

		return "redirect:/";
	}

	@GetMapping("/{id}")
	public String read(Model model, @PathVariable("id") Long id, @AuthenticationPrincipal UserResponseDto principal) {
		log.info("/boardGet/////////////////////////////////////////////////////////////////");

		boardRepo.findById(id).ifPresent(board -> {
			
			board.setViewCount(board.getViewCount() + 1);
			boardRepo.save(board);

			board.setBoardContent(board.getBoardContent().replace(System.lineSeparator(), "<br>"));
			log.info("board : {}", board);
			model.addAttribute("board", board);

			List<LikeEntity> likes = likeRepo.findAllByBoard(board);
			model.addAttribute("likes", likes);

			if (principal != null) {
				likeRepo.findByBoardAndUser(board, principal.toEntity()).ifPresent(consumer -> {
					model.addAttribute("like", consumer);
				});
			}
		});


		return "board/page-single-topic";
	}

	@ResponseBody
	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") Long id) {
		log.info("====================================Delete Mapping=================================");
		log.info(id.toString());
		boardRepo.deleteById(id);
		return "성공적으로 삭제되었습니다.";
	}

	@ResponseBody
	@PutMapping("/{id}")
	public String put(@PathVariable("id") Long id, @ModelAttribute BoardEntity board, String categoryName) {
		log.info("====================================Put Mapping=================================");

		if ("".equals(categoryName) && categoryName == null) {
			return "";
		}
		// categoryName 대신 번호로 받기
		// 글쓸 때마다 db를 2번 감

		log.info(id.toString());
		log.info(board.toString());
		
		boardRepo.updateById(id, board.getBoardTitle(), board.getBoardContent());
		return "성공적으로 수정되었습니다.";
	}

	@Secured(value={"ROLE_USER", "ROLE_ADMIN"})
	@GetMapping("/{id}/edit")
	public String editGet(Model model, @PathVariable("id") Long id) {
		boardRepo.findById(id).ifPresent(value -> model.addAttribute("board", value));

		List<CategoryEntity> categories =
			StreamSupport.stream(categoryRepo.findAll().spliterator(), false)
    							 .collect(Collectors.toList());
		model.addAttribute("categories", categories);

		return "board/write";
	}

	/*
		클라이언트는 @MessageMapping 으로 request
		서버는 @SendTo 로 response
	*/
	// return String
	@MessageMapping("/nbax") // 전역 RequestMapping(여기서 '/board')에 적용되지 않는다. -> 따로 컨트롤러를 두자(MessageController)
	@SendTo("/topic/messagexx")	// publishing
	public String newBoardAlertx(String message) throws Exception {
		log.info("STOMP >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + message);
		return message;
	}
	
	// return Object
	@MessageMapping("/nbaxx")
	@SendTo("/topic/messagexx")	// publishing
	public String newBoardAlertxx(String message) throws Exception {
		log.info("STOMP >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + message);
		return message;
	}
	
}
