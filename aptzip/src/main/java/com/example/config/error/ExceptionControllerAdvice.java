package com.example.config.error;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {

  /**
   * @valid 유효성체크에 통과하지 못하면 MethodArgumentNotValidException 이 발생한다.
   * 
   * 왜 여기로 안오고 DefaultHandlerExceptionResolver 로만 향할까...
   */
  // @ExceptionHandler(MethodArgumentNotValidException.class)
  // public ResponseEntity<ErrorResponse> methodValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
  //   log.warn("===============================LoginSuccessHandler-Constructor=====================================");
  //   log.error("MethodArgumentNotValidException occur!!! url:{}, trace:{}", request.getRequestURI(), e.getStackTrace());
  //   ErrorResponse errorResponse = makeErrorResponse(e.getBindingResult());
  //   return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
  // }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public void methodValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
    log.warn("===============================LoginSuccessHandler-Constructor=====================================");
    log.error("MethodArgumentNotValidException occur!!! url:{}, trace:{}", request.getRequestURI(), e.getStackTrace());
    // ErrorResponse errorResponse = makeErrorResponse(e.getBindingResult());
    // return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
  }
  
  private ErrorResponse makeErrorResponse(BindingResult bindingResult) {
    String code = "";
    String description = "";
    String detail = "";

    // 에러가 있다면
    if (bindingResult.hasErrors()) {
      // DTO에 설정한 meaasge값을 가져온다
      detail = bindingResult.getFieldError().getDefaultMessage();

      // DTO에 유효성체크를 걸어놓은 어노테이션명을 가져온다.
      String bindResultCode = bindingResult.getFieldError().getCode();

      switch (bindResultCode) {
      case "NotNull":
        code = ErrorCode.NOT_NULL.getCode();
        description = ErrorCode.NOT_NULL.getDescription();
        break;
      case "Min":
        code = ErrorCode.MIN_VALUE.getCode();
        description = ErrorCode.MIN_VALUE.getDescription();
        break;
      }
    }

    return new ErrorResponse(code, description, detail);
  }
}