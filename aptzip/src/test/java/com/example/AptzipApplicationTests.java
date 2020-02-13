package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

// @Slf4j
@SpringBootTest
@Commit
class AptzipApplicationTests {

	// @Autowired
	// private BoardRepository boardRepository;
	
	// @Autowired
	// private CommentRepository commentRepository;

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
