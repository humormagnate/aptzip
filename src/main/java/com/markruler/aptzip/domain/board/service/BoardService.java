package com.markruler.aptzip.domain.board.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.markruler.aptzip.domain.apartment.model.AptEntity;
import com.markruler.aptzip.domain.apartment.model.AptRequestDto;
import com.markruler.aptzip.domain.board.model.BoardEntity;
import com.markruler.aptzip.domain.board.model.BoardRequestDto;
import com.markruler.aptzip.domain.board.model.Category;
import com.markruler.aptzip.domain.board.model.CustomPage;
import com.markruler.aptzip.domain.board.repository.BoardRepository;
import com.markruler.aptzip.domain.user.model.UserAccountEntity;
import com.markruler.aptzip.domain.user.model.UserAccountRequestDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class BoardService {
  Logger log = LoggerFactory.getLogger(BoardService.class);

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
      BoardRequestDto board = BoardRequestDto.of(boardEntity.get());
      board.increaseViewCount();
      BoardEntity entity = boardRepository.save(board.toEntity());
      board.setContent(entity.getContent().replace(System.lineSeparator(), "<br>"));
      return entity;
    }
    return null;
  }

  public List<BoardEntity> listBoardsByApt(UserAccountRequestDto user) {
    AptEntity apt = AptRequestDto.builder().code(user.getApt().getCode()).build().toEntity();
    return StreamSupport.stream(boardRepository.findAllByApt(apt).spliterator(), false).collect(Collectors.toList());
  }

  public BoardEntity save(BoardRequestDto board, String category, UserAccountEntity user) {
    log.debug("board.getUser: {}", board.getUser());
    board.setCategory(Category.valueOf(category));
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
