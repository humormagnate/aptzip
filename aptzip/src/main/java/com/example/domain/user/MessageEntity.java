package com.example.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageEntity {
	// @NonNull 인 필드는 RequiredArgsConstructor 에 포함 (NonNull 말고도 해당되는 어노테이션이 있다. 문서 참고.)
	private String msg;
	private AptzipUserEntity user;
}