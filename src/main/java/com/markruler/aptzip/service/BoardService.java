package com.markruler.aptzip.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.markruler.aptzip.domain.apartment.AptEntity;
import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.domain.board.BoardRequestDto;
import com.markruler.aptzip.domain.board.CategoryEntity;
import com.markruler.aptzip.domain.board.LikeEntity;
import com.markruler.aptzip.domain.user.UserRequestDto;
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
  public Page<BoardEntity> listBoardByPage(String apartmentCode, CustomPage customPage) {
    if (apartmentCode != null && !apartmentCode.isEmpty()) {
      customPage.setAptCode(apartmentCode);
    }
    return boardRepository.findBoardByDynamicQuery(customPage.makePageable(true, "id"), customPage);
  }

  public void findById(Long boardId, UserRequestDto user, Model model) {
    boardRepository.findById(boardId).ifPresent(board -> {
      board.setViewCount(board.getViewCount() + 1);
      boardRepository.save(board);
      board.setBoardContent(board.getBoardContent().replace(System.lineSeparator(), "<br>"));
      model.addAttribute("board", board);

      LikeEntity likeEntity = LikeEntity.builder().board(board).build();
      List<LikeEntity> likes = likeRepository.findAllByBoard(likeEntity.getBoard());
      model.addAttribute("likes", likes);

      if (user != null) {
        likeEntity.setUser(user.toEntity());
        Optional<LikeEntity> like = likeRepository.findByBoardAndUser(likeEntity.getBoard(), likeEntity.getUser());
        model.addAttribute("like", like.orElse(null));
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

  public List<BoardEntity> listBoardsByApt(UserRequestDto user) {
    AptEntity apt = AptEntity.builder().code(user.getApt().getCode()).build();
    return StreamSupport.stream(boardRepository.findAllByApt(apt).spliterator(), false).collect(Collectors.toList());
  }

  public BoardEntity save(BoardRequestDto boardDTO, String categoryId, UserRequestDto user) {
    // @formatter:off
    BoardRequestDto.builder()
      .category(new CategoryEntity(Long.valueOf(categoryId)))
      .isEnabled(true)
      .viewCount(0L)
      .user(user.toEntity())
      .apt(boardDTO.getUser().getApt())
      .updateDate(LocalDateTime.now());
    // @formatter:on
    return boardRepository.save(boardDTO.toEntity());
  }

  public void deleteById(Long id) {
    boardRepository.deleteById(id);
  }

  public void updateById(BoardRequestDto board) {
    // @formatter:off
    boardRepository.updateById(
			board.getId(),
			board.getBoardTitle(),
      board.getBoardContent(),
      board.getCategory()
    // @formatter:on
    );
  }
}
