package com.markruler.aptzip.infrastructure.error;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ErrorResponse {

  private HttpStatus status;
  private String message;

  public static ErrorResponse of(final HttpStatus status, final String message) {
    return new ErrorResponse(status, message);
  }

}
