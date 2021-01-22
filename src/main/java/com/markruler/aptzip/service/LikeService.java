package com.markruler.aptzip.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.domain.board.LikeEntity;
import com.markruler.aptzip.domain.user.UserResponseDto;
import com.markruler.aptzip.persistence.board.LikeRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@lombok.extern.slf4j.Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {
  private final LikeRepository likeRepository;

  public List<LikeEntity> findAll() {
    return StreamSupport.stream(likeRepository.findAll().spliterator(), false)
        .collect(Collectors.toList());
  }

  public String readByBoardAndUser(Long boardID, LikeEntity like, UserResponseDto principal) {
    BoardEntity board = new BoardEntity();
    board.setId(Long.valueOf(boardID));

    LikeEntity existLike =
        likeRepository.findByBoardAndUser(board, principal.toEntity()).orElse(null);
    log.debug("exist like : {}", existLike);

    if (existLike == null) {
      like.setBoard(board);
      like.setUser(principal.toEntity());
      likeRepository.save(like);
      return "insert";
    } else {
      likeRepository.deleteById(existLike.getId());
      return "delete";
    }
  }
}
