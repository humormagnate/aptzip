package com.markruler.aptzip.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AlertMessage {
	private String msg;
	private UserAccountEntity user;
}