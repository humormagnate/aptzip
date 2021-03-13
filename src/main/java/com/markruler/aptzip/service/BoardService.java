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
import com.markruler.aptzip.domain.board.Category;
import com.markruler.aptzip.domain.user.UserAccountEntity;
import com.markruler.aptzip.domain.user.UserAccountRequestDto;
import com.markruler.aptzip.helper.CustomPage;
import com.markruler.aptzip.persistence.board.BoardRepository;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class BoardService {
  private final BoardRepository boardRepository;

  @Transactional(readOnly = true)
  public Page<BoardEntity> listBoardByPage(String apartmentCode, CustomPage customPage) {
    if (apartmentCode != null && !apartmentCode.isEmpty()) {
      customPage.setAptCode(apartmentCode);
    }
    return boardRepository.findBoardByDynamicQuery(customPage.makePageable(true, "id"), customPage);
  }

  public BoardEntity findById(Long boardId) {
    Optional<BoardEntity> boardEntity = boardRepository.findById(boardId);
    if (boardEntity.isPresent()) {
      // FIXME: Entity to DTO
      BoardEntity board = boardEntity.get();
      board.setViewCount(board.getViewCount() + 1);
      boardRepository.save(board);
      board.setContent(board.getContent().replace(System.lineSeparator(), "<br>"));
      return board;
    }
    return null;
  }

  public List<BoardEntity> listBoardsByApt(UserAccountRequestDto user) {
    AptEntity apt = AptRequestDto.builder().code(user.getApt().getCode()).build().toEntity();
    return StreamSupport.stream(boardRepository.findAllByApt(apt).spliterator(), false).collect(Collectors.toList());
  }

  public BoardEntity save(BoardRequestDto board, String categoryId, UserAccountEntity user) {
    board.setCategory(Category.valueOf(categoryId));
    board.setEnabled(true);
    board.setViewCount(0L);
    board.setUser(user);
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
			board.getTitle(),
      board.getContent(),
      board.getCategory()
    // @formatter:on
    );
  }
}
