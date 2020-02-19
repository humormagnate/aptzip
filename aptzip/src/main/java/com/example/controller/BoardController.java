package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.domain.board.BoardEntity;
import com.example.domain.board.CategoryEntity;
import com.example.domain.user.UserResponseDto;
import com.example.persistence.BoardRepository;
import com.example.persistence.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;


@Controller
@RequestMapping("/board/")
@Slf4j
public class BoardController {

	@Autowired private BoardRepository boardRepo;
	@Autowired private CategoryRepository categoryRepo;
	@Autowired private SimpMessageSendingOperations smso;

	@Deprecated
	@GetMapping("/list")
	public ModelAndView list(ModelAndView mv) {
		List<BoardEntity> list = new ArrayList<BoardEntity>();
		for (BoardEntity str : boardRepo.findAll()) {
			list.add(str);
		}
		log.info(list.toString());
		
		mv.addObject("list", list)
			.setViewName("board/list");
		
		return mv;
	}

	@GetMapping("/write")
	public void writeGet() {}

	// AuthenticationPrincipal은 Context에 있기 때문에 계속 보내줄 필요 없음
	// public ModelAndView writeGet(@AuthenticationPrincipal UserResponseDto principal, ModelAndView mv) {
	// 	mv.addObject("principal", principal)
	// 		.setViewName("board/page-create-topic");
	// 	return mv;
	// }
	
	// UserDetails를 구현한 UserRequestDto는 @AuthenticationPrincipal 이 안먹히고
	// UserDetails를 구현한 User를 상속한 UserResponseDto는 먹힌다. Why?
	@PostMapping("/write")
	public String writePost(BoardEntity board, @AuthenticationPrincipal UserResponseDto principal, RedirectAttributes rttr, String categoryName) {
		
		log.info("/board/write/post////////////////////////////////////////////////////////////////////////////");
		log.info(board.toString());
		log.info(principal.toString());

		CategoryEntity category = categoryRepo.findByName(categoryName).get();
		board.setCategory(category);
		board.setBoardStatus("Y");
		board.setUser(principal.toEntity());
		board.setApt(board.getUser().getApt());

		log.info(board.toString());
		log.info(board.getUser().getApt().toString());

		boardRepo.save(board);

		/*
			Post-Redirect-Get 방식
			: 리다이렉트를 하지 않으면 사용자가 여러 번 게시물을 등록할 수 있기 때문에 이를 방지하기 위함
		*/
		rttr.addFlashAttribute("msg", "success");

		return "redirect:/";
	}

	@GetMapping("/{id}")
	public String read(Model model, @PathVariable("id") Long id) {
		log.info("/boardGet/////////////////////////////////////////////////////////////////");

		boardRepo.findById(id).ifPresent(board -> {
			
			board.setViewCount(board.getViewCount() + 1);
			boardRepo.save(board);

			board.setBoardContent(board.getBoardContent().replace(System.lineSeparator(), "<br>"));
			model.addAttribute("board", board);
		});
		
		model.addAttribute("newLineChar", "\r");

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
	public String put(@PathVariable("id") Long id, @ModelAttribute BoardEntity board) {
		log.info("====================================Put Mapping=================================");
		log.info(id.toString());
		log.info(board.toString());
		
		boardRepo.updateById(id, board.getBoardTitle(), board.getBoardContent());
		return "성공적으로 수정되었습니다.";
	}

	@Secured(value={"ROLE_USER", "ROLE_ADMIN"})
	@GetMapping("/edit/{id}")
	public String editGet(Model model, @PathVariable("id") Long id) {
		boardRepo.findById(id).ifPresent(value -> model.addAttribute("board", value));
		return "board/write";
	}

	@Secured(value = { "ROLE_USER", "ROLE_ADMIN" })
	@PutMapping("/edit/{id}")
	public String editPut(@PathVariable("id") Long id, BoardEntity board) {
			boardRepo.findById(id).ifPresent(origin -> {
				origin.setCategory(board.getCategory());
				origin.setBoardTitle(board.getBoardTitle());
				origin.setBoardContent(board.getBoardContent());
				boardRepo.save(origin);
			});
			return "redirect:/board/" + id;
	}

	// @Deprecated
	// @PostMapping("/edit/{id}")
	// public String editPost(BoardEntity board, RedirectAttributes rttr) {
	// 	boardRepository.findById(board.getId()).ifPresent(origin -> {
	// 		origin.setCategory(board.getCategory());
	// 		origin.setBoardTitle(board.getBoardTitle());
	// 		origin.setBoardContent(board.getBoardContent());
	// 		boardRepository.save(origin);
	// 	});
	// 	return "redirect:/board/" + board.getId();
	// }

	@Secured(value = { "ROLE_USER", "ROLE_ADMIN" })
	@GetMapping("/del")
	public String delGET(Long id) {
		boardRepo.deleteById(id);
		return "redirect:/";
	}

	// @PostMapping(value="/search")
	// public ModelAndView retrieve(ModelAndView mv) {
	// 	return mv;
	// }

	/*
		클라이언트는 @MessageMapping 으로 request
		서버는 @SendTo 로 response
	*/
	// return String
	@MessageMapping("/nbax") // 전역 RequestMapping(여기서 '/board')에 적용되지 않는다. -> 따로 컨트롤러를 두자
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
