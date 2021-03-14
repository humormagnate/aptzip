package com.markruler.aptzip.infrastructure.error;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;

@Controller
@Api(tags = "error")
public class ErrorResponseController implements ErrorController {
  Logger log = LoggerFactory.getLogger(ErrorResponseController.class);

  private static final String ERROR_PATH = "error/error";

  @Override
  public String getErrorPath() {
    return ERROR_PATH;
  }

  @GetMapping(value = { "${server.error.path}" }, produces = MediaType.TEXT_HTML_VALUE)
  public String handleError(HttpServletRequest request, Model model) {
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    HttpStatus httpStatus = HttpStatus.valueOf(Integer.valueOf(status.toString()));
    if (!httpStatus.is2xxSuccessful()) {
      log.error("HTTP Status: {}", status.toString());
      log.error("Reason Phrase: {}", httpStatus.getReasonPhrase());
      // @formatter:off
      model
        .addAttribute("code", status.toString())
        .addAttribute("status", httpStatus.getReasonPhrase());
      // @formatter:on
    }
    return ERROR_PATH;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.error("handleMethodArgumentNotValidException", e);
    final ErrorResponse response = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getBindingResult(), "");
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

}