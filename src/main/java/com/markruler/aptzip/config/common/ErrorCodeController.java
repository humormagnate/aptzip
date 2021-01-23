package com.markruler.aptzip.config.common;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

// ConfigurableServletWebServerFactory -> addErrorPages
// org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController
@Slf4j
@Controller
public class ErrorCodeController implements ErrorController {

  private static final String ERROR_PATH = "error/error";
  
  @Override
  public String getErrorPath() {
    log.debug("================================get error path================================");
    return ERROR_PATH;
  }

  @GetMapping(value = {"${server.error.path}"}, produces = MediaType.TEXT_HTML_VALUE)
  public String getException(HttpServletRequest request, Model model) {
    log.debug("============================just error page (getException)============================");
    
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    log.debug("status is null ? {}", status);
    if (status != null) {
      HttpStatus httpStatus = HttpStatus.valueOf(Integer.valueOf(status.toString()));
      log.debug("httpStatus : {}", httpStatus);
      log.debug("httpStatus.toString() : {}", httpStatus.toString());
      log.debug("httpStatus.ordinal() : {}", httpStatus.ordinal());
      log.debug("httpStatus.getReasonPhrase() : {}", httpStatus.getReasonPhrase());
      log.debug("status.toString() : {}", status.toString());
  
      model.addAttribute("code", status.toString())
           .addAttribute("msg", httpStatus.getReasonPhrase());
    }
    
    return ERROR_PATH;
  }
  
  @GetMapping(value = "/denied", produces = MediaType.TEXT_HTML_VALUE)
  public void accessDenied() {
    log.debug("================================ denied ================================");
  }

}