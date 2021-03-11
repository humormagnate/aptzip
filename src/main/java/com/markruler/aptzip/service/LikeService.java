package com.markruler.aptzip.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.markruler.aptzip.domain.board.LikeEntity;
import com.markruler.aptzip.domain.board.LikeRequestDto;
import com.markruler.aptzip.persistence.board.LikeRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {
  private final LikeRepository likeRepository;

  public List<LikeEntity> findAll() {
    return StreamSupport.stream(likeRepository.findAll().spliterator(), false).collect(Collectors.toList());
  }

  public LikeEntity save(LikeRequestDto like) {
    return likeRepository.save(like.toEntity());
  }

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
