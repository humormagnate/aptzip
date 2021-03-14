package com.markruler.aptzip.infrastructure.error;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ErrorResponse {

  private HttpStatus code;
  private List<FieldError> status;
  private String message;

  public static ErrorResponse of(final HttpStatus code, final BindingResult status, final String message) {
    return new ErrorResponse(code, status.getFieldErrors(), message);
  }

}
