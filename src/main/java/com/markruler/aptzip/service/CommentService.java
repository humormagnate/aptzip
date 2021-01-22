package com.markruler.aptzip.service;

import java.util.List;
import com.markruler.aptzip.domain.board.BoardEntity;
import com.markruler.aptzip.domain.board.CommentEntity;
import com.markruler.aptzip.domain.user.UserResponseDto;
import com.markruler.aptzip.persistence.board.CommentRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;

  public List<CommentEntity> listComments(Long boardId) {
    // TODO: Make a builder pattern
    BoardEntity board = new BoardEntity();
    board.setId(boardId);
    List<CommentEntity> list = commentRepository.getCommentsByBoardId(board);
    if (list != null && list.size() > 0) {
      list.forEach(comment -> {
        // board는 저장하는 객체를 바로 변경하니까 System.lineSeparator()에서도 변경되지만,
        // comment는 MySQL에서 가져오기 때문에 "\n" 지정해줘야함.
        // (리눅스 환경에서도 가능할지 문제, "\r"은 안됨: 어차피 리눅스는 "\n", 윈도가 "\r\n")
        comment.setCommentContent(comment.getCommentContent().replace("\n", "<br>"));
      });
    }
    // @formatter:off
    // list.forEach(comment -> {
    //  comment.setCommentContent(comment.getCommentContent().replace(System.lineSeparator(),
    //  "<br>"));
    // });
    // if (list != null && list.size() > 0) {
    //   list.get(0).setCommentContent(
    //       list.get(0).getCommentContent().replace(System.lineSeparator(), "<br>"));
    // }
    // @formatter:on
    return list;
  }

  public boolean create(Long boardId, CommentEntity comment, UserResponseDto principal) {
    BoardEntity board = new BoardEntity();
    board.setId(boardId);

    comment.setUser(principal.toEntity());
    comment.setBoard(board);
    comment.setCommentStatus("Y");
    CommentEntity result = commentRepository.save(comment);

    return result != null;
  }

  public void deleteById(Long commentId) {
    commentRepository.deleteById(commentId);
  }

  public void updateComment(CommentEntity comment) {
    commentRepository.findById(comment.getId()).ifPresent(origin -> {
      origin.setCommentContent(comment.getCommentContent());
      commentRepository.save(origin);
    });
  }
}
