package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@SpringBootTest
@Commit
class AptzipApplicationTests {

	private final PasswordEncoder passwordEncoder;
	// private final BoardRepository boardRepository;
	// private final CommentRepository commentRepository;

	@Test
	public void password() {
		for (int i = 0; i < 10; i++) {
			log.info(passwordEncoder.encode("qwe"));
		}
	}

	@Test
	void contextLoads() {
	}

	// @Test
	// public void insertDummy(){
	// 	IntStream.range(1, 200).forEach(i -> {
	// 		BoardEntity board = new BoardEntity();
	// 		board.setBoardTitle("Title..." + i);
	// 		board.setBoardContent("Content..." + i);
	// 		board.setUserId(AptzipUserEntity.builder().id(2).build());
	// 		board.setAptId(AptEntity.builder().aptId(3).build());
	// 		boardRepository.save(board);
	// 	});
	// }
}
