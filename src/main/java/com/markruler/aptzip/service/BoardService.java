package com.markruler.aptzip.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import com.markruler.aptzip.domain.apartment.AptEntity;
import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.domain.board.BoardRequestDto;
import com.markruler.aptzip.domain.board.CategoryEntity;
import com.markruler.aptzip.domain.board.LikeEntity;
import com.markruler.aptzip.domain.user.UserResponseDto;
import com.markruler.aptzip.helper.CustomPage;
import com.markruler.aptzip.persistence.board.BoardRepository;
import com.markruler.aptzip.persistence.board.CategoryRepository;
import com.markruler.aptzip.persistence.board.LikeRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
  private final BoardRepository boardRepository;
  private final LikeRepository likeRepository;
  private final CategoryRepository categoryRepository;

  @Transactional(readOnly = true)
  public Page<BoardEntity> listBoardByPage(Long apartmentID, CustomPage customPage) {
    if (apartmentID != 0) {
      // 아파트 ID를 받아 해당 아파트의 thread만 받음
      customPage.setAptId(apartmentID);
    }
    return boardRepository.findBoardByDynamicQuery(customPage.makePageable(true, "id"), customPage);
  }

  public void findById(Long id, UserResponseDto principal, Model model) {
    boardRepository.findById(id).ifPresent(board -> {
      board.setViewCount(board.getViewCount() + 1);
      boardRepository.save(board);

      board.setBoardContent(board.getBoardContent().replace(System.lineSeparator(), "<br>"));
      model.addAttribute("board", board);

      List<LikeEntity> likes = likeRepository.findAllByBoard(board);
      model.addAttribute("likes", likes);

      if (principal != null) {
        likeRepository.findByBoardAndUser(board, principal.toEntity()).ifPresent(consumer -> {
          model.addAttribute("like", consumer);
        });
      }
    });
  }

  public List<CategoryEntity> findByIdFromEdit(Long id, Model model) {
    boardRepository.findById(id).ifPresent(value -> model.addAttribute("board", value));
    // @formatter:off
    return StreamSupport
            .stream(categoryRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
    // @formatter:on
  }

  public List<BoardEntity> listBoardsByAPT(UserResponseDto principal) {
    AptEntity apt = AptEntity.builder().id(principal.getApt().getId()).build();
    return StreamSupport.stream(boardRepository.findAllByApt(apt).spliterator(), false)
        .collect(Collectors.toList());
  }

  public boolean save(BoardEntity board, String categoryId, UserResponseDto principal) {
    board.setCategory(new CategoryEntity(Long.valueOf(categoryId)));
    board.setBoardStatus("Y");
    board.setUser(principal.toEntity());
    board.setApt(board.getUser().getApt());
    board.setUpdateDate(LocalDateTime.now());
    BoardEntity ret = boardRepository.save(board);
    return ret != null;
  }

  public void deleteById(Long id) {
    boardRepository.deleteById(id);
  }

  public void updateById(BoardRequestDto board) {
    boardRepository.updateById(
    // @formatter:off
			board.getId(),
			board.getBoardTitle(),
			board.getBoardContent(),
      new CategoryEntity(board.getCategoryId())
    // @formatter:on
    );
  }
}
