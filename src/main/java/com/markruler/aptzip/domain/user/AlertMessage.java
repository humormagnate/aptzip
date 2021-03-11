package com.markruler.aptzip.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlertMessage {
	private String msg;
	private UserAccountEntity user;
}