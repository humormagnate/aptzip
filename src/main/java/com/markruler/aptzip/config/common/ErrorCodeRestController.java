package com.markruler.aptzip.config.common;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ErrorCodeRestController implements ErrorController {

  private static final String REST_ERROR_MESSAGE = "error";
  
  @Override
  public String getErrorPath() {
    log.debug("================================get error path================================");
    return REST_ERROR_MESSAGE;
  }

  @GetMapping({"${server.error.path}"})
  public String getException(HttpServletRequest request, Model model) {
    log.debug("============================just error page (getException)============================");
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    log.debug("status is null ? {}", status);
    HttpStatus httpStatus = HttpStatus.valueOf(Integer.valueOf(status.toString()));
    log.debug("httpStatus : {}", httpStatus);
    log.debug("httpStatus.toString() : {}", httpStatus.toString());
    log.debug("httpStatus.ordinal() : {}", httpStatus.ordinal());
    log.debug("httpStatus.getReasonPhrase() : {}", httpStatus.getReasonPhrase());
    log.debug("status.toString() : {}", status.toString());

    model.addAttribute("code", status.toString())
         .addAttribute("msg", httpStatus.getReasonPhrase());
    
    return REST_ERROR_MESSAGE;
  }
  
  @GetMapping("/denied")
  public String accessDenied() {
    log.debug("================================ denied ================================");
    return REST_ERROR_MESSAGE;
  }

}