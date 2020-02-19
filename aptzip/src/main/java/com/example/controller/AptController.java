package com.example.controller;

import com.example.persistence.BoardRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/{id}")
public class AptController {

  @Autowired
  private BoardRepository boardRepo;

  @GetMapping
  public void thread() {
    // apt id를 받아 해당 아파트의 thread만 받음
  }
}