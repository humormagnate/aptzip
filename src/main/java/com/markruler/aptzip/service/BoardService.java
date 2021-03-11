package com.markruler.aptzip.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.markruler.aptzip.domain.apartment.AptEntity;
import com.markruler.aptzip.domain.apartment.AptRequestDto;
import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.domain.board.BoardRequestDto;
import com.markruler.aptzip.domain.board.CategoryEntity;
import com.markruler.aptzip.domain.board.CategoryRequestDto;
import com.markruler.aptzip.domain.board.LikeEntity;
import com.markruler.aptzip.domain.board.LikeRequestDto;
import com.markruler.aptzip.domain.user.UserAccountRequestDto;
import com.markruler.aptzip.helper.CustomPage;
import com.markruler.aptzip.persistence.board.BoardRepository;
import com.markruler.aptzip.persistence.board.CategoryRepository;
import com.markruler.aptzip.persistence.board.LikeRepository;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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

  public void findById(Long boardId, UserAccountRequestDto user, Model model) {
    boardRepository.findById(boardId).ifPresent(board -> {
      board.setViewCount(board.getViewCount() + 1);
      boardRepository.save(board);
      board.setBoardContent(board.getBoardContent().replace(System.lineSeparator(), "<br>"));
      model.addAttribute("board", board);

      LikeRequestDto like = LikeRequestDto.builder().board(board).build();
      List<LikeEntity> likes = likeRepository.findAllByBoard(like.getBoard());
      model.addAttribute("likes", likes);

      if (user != null) {
        like.setUser(user.toEntity());
        Optional<LikeEntity> likeEntity = likeRepository.findByBoardAndUser(like.getBoard(), like.getUser());
        model.addAttribute("like", likeEntity.orElse(null));
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

  public List<BoardEntity> listBoardsByApt(UserAccountRequestDto user) {
    AptEntity apt = AptRequestDto.builder().code(user.getApt().getCode()).build().toEntity();
    return StreamSupport.stream(boardRepository.findAllByApt(apt).spliterator(), false).collect(Collectors.toList());
  }

  public BoardEntity save(BoardRequestDto board, String categoryId, UserAccountRequestDto user) {
    board.setCategory(CategoryRequestDto.builder().id(Long.valueOf(categoryId)).build().toEntity());
    board.setIsEnabled(true);
    board.setViewCount(0L);
    board.setUser(user.toEntity());
    board.setApt(user.getApt());
    board.setUpdateDate(LocalDateTime.now());
    log.debug("board.getUser: {}", board.getUser());
    return boardRepository.save(board.toEntity());
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
