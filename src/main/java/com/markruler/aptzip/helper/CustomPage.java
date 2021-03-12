package com.markruler.aptzip.helper;

import com.markruler.aptzip.domain.board.Category;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class CustomPage {

  private static final int DEFAULT_SIZE = 10;
  private static final int DEFAULT_MAX_SIZE = 50;

  private int page; // 현재 페이지 - 1
  private int size; // 한 페이지당 게시물 개수

  private String query; // 검색어
  private Integer limit; // 검색결과 개수 제한
  private String writer;
  private Boolean liked;
  private Category category;
  private String date;
  private String aptCode;

  public CustomPage() {
    this.page = 1;
    this.size = DEFAULT_SIZE;
  }

  public void setPage(int page) {
    this.page = page < 0 ? 1 : page;
  }

  public void setSize(int size) {
    this.size = size < DEFAULT_SIZE || size > DEFAULT_MAX_SIZE ? DEFAULT_SIZE : size;
  }

  public Pageable makePageable(boolean direction, String... props) {
    Sort.Direction dir = direction ? Sort.Direction.DESC : Sort.Direction.ASC;
    return PageRequest.of(this.page - 1, this.size, dir, props);
  }

}
