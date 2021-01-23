package com.markruler.aptzip.helper;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// @lombok.extern.slf4j.Slf4j
@Getter
@Setter
@ToString(exclude = "pageList")
public class CustomPageMaker<T> {
  private int currentPageNum;
  private int totalPageNum;

  private Page<T> result;
  private Pageable prevPage;
  private Pageable nextPage;
  private Pageable currentPage;
  private List<Pageable> pageList; // 페이지 번호의 Pageable들을 저장할 리스트

  public CustomPageMaker(Page<T> result) {
    this.result = result;
    this.currentPage = result.getPageable();
    this.currentPageNum = currentPage.getPageNumber() + 1;
    this.totalPageNum = result.getTotalPages();
    this.pageList = new ArrayList<>();
    calcPages();
  }

  private void calcPages() {
    int tempEndNum = (int) (Math.ceil(this.currentPageNum / 3.0) * 3); // 해당 page의 끝번호
    int startNum = tempEndNum - 2; // 해당 page의 시작번호
    Pageable startPage = this.currentPage;

    for (int i = startNum; i < this.currentPageNum; i++) {
      startPage = startPage.previousOrFirst();
    }
    this.prevPage = startPage.getPageNumber() <= 0 ? null : startPage.previousOrFirst();

    if (this.totalPageNum < tempEndNum) {
      tempEndNum = this.totalPageNum;
      this.nextPage = null;
    }

    for (int i = startNum; i <= tempEndNum; i++) {
      pageList.add(startPage);
      startPage = startPage.next();
    }
    this.nextPage = startPage.getPageNumber() < totalPageNum ? startPage : null;
  }

}
