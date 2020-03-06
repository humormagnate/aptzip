package com.markruler.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageEntity {
	private String msg;
	private AptzipUserEntity user;
}