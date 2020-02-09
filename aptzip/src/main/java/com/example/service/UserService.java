package com.example.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.UserRequestDto;
import com.example.domain.UserResponseDto;
import com.example.persistence.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

  @Transactional
  public int save(UserRequestDto userRequestDto){
    return userRepository.save(userRequestDto.toEntity()).getUserId();
  }

  @Transactional(readOnly = true)
  public List<UserResponseDto> findAll() {
    return userRepository.findAll()
						             .stream()
						             .map(UserResponseDto::new)
						             .collect(Collectors.toList());
  }
  
  
}
