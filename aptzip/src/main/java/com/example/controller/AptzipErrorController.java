package com.example.controller;

import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
// @RequestMapping({"${server.error.path:${error.path:/error}}"})
public class AptzipErrorController implements ErrorController {

  @GetMapping(value = "/error/{statusCode}")
  public ModelAndView getErrorPage(@PathVariable("statusCode") String statusCode, HttpServletRequest request, HttpServletResponse response,
                                            ModelAndView mv) {
    // Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    // if (status != null) {
    //   if (String.valueOf(status).equalsIgnoreCase(HttpStatus.NOT_FOUND.toString())) {
    //   }
    // }

    //https://supawer0728.github.io/2019/04/04/spring-error-handling/
    // HttpStatus status = getStatus(request);
    // Map<String, Object> model = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.TEXT_HTML)));
		
    // response.setStatus(status.value());
    // ModelAndView modelAndView = resolveErrorView(request, response, status, model);
    
    // return (modelAndView != null) ? modelAndView : new ModelAndView("error", model);

    // resolveErrorView(request, response, status, model);
    // Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
    
    log.info("=======================================erro-controller================================");
    log.info(statusCode);

    mv.addObject("statusCode", statusCode)
      .setViewName("/error");
    
    return mv;
  }

  @Override
  public String getErrorPath() {
    return "/error";
  }

}