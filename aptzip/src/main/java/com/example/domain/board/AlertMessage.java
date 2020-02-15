package com.example.domain.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class AlertMessage {
	// @NonNull 인 필드는 RequiredArgsConstructor 에 포함 (NonNull 말고도 해당되는 어노테이션이 있다. 문서 참고.)
	@NonNull private String msg;
	private String boardWriter;
	private String commentWriter;
}