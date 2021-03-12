package com.markruler.aptzip.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@lombok.extern.slf4j.Slf4j
public class ErrorCodeController implements ErrorController {

  private static final String ERROR_PATH = "error/error";

  @Override
  public String getErrorPath() {
    return ERROR_PATH;
  }

  @GetMapping(value = {"${server.error.path}"}, produces = MediaType.TEXT_HTML_VALUE)
  public String handleError(HttpServletRequest request, Model model) {
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    HttpStatus httpStatus = HttpStatus.valueOf(Integer.valueOf(status.toString()));
    if (!httpStatus.is2xxSuccessful()) {
      log.debug("Request URL: {}", request.getRequestURL());
      log.debug("HTTP Status: {}", status.toString());
      log.debug("Reason Phrase: {}", httpStatus.getReasonPhrase());

      // @formatter:off
      model
        .addAttribute("code", status.toString())
        .addAttribute("message", httpStatus.getReasonPhrase());
      // @formatter:on
    }

    return ERROR_PATH;
  }

}
