package com.example.vo;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.Getter;

@Getter
public class PageVo {

	private static final int DEFAULT_SIZE = 10;
	private static final int DEFAULT_MAX_SIZE = 50;

	private int page;	// 현재 페이지 - 1
	private int size;	// 한 페이지당 게시글 개수
	
	private String keyword;
	private String type;

	public PageVo() {
		this.page = 1;
		this.size = DEFAULT_SIZE;
	}
	

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setPage(int page) {
		this.page = page < 0 ? 1 : page;
	}


	public void setSize(int size) {
		this.size = size < DEFAULT_SIZE || size > DEFAULT_MAX_SIZE ? DEFAULT_SIZE : size;
	}

	public Pageable makePageable(int direction, String... props) {
		Sort.Direction dir = direction == 0 ? Sort.Direction.DESC : Sort.Direction.ASC;
		return PageRequest.of(this.page - 1, this.size, dir, props);
	}

}