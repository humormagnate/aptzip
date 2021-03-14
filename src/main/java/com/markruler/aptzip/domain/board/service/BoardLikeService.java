package com.markruler.aptzip.domain.board.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.markruler.aptzip.domain.board.model.BoardEntity;
import com.markruler.aptzip.domain.board.model.LikeEntity;
import com.markruler.aptzip.domain.board.model.LikeRequestDto;
import com.markruler.aptzip.domain.board.repository.LikeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardLikeService implements LikeService {
  Logger log = LoggerFactory.getLogger(BoardLikeService.class);

  private final LikeRepository likeRepository;

  @Override
  public LikeEntity save(LikeRequestDto like) {
    return likeRepository.save(like.toEntity());
  }

  @Override
  public List<LikeEntity> findAll() {
    return StreamSupport.stream(likeRepository.findAll().spliterator(), false).collect(Collectors.toList());
  }

  @Override
  public List<LikeEntity> findLikesByBoard(BoardEntity board) {
    LikeRequestDto like = LikeRequestDto.builder().board(board).build();
    return likeRepository.findAllByBoard(like.getBoard());
  }

  @Override
  public void delete(LikeRequestDto like) {
    Optional<LikeEntity> existLike = likeRepository.findByBoardAndUser(like.getBoard(), like.getUser());
    if (existLike.isPresent()) {
      log.debug("Delete the like");
      likeRepository.deleteById(like.getId());
    } else {
      log.debug("The like not found");
    }
  }

}
