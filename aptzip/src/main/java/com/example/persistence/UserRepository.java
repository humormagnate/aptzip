package com.example.persistence;


import java.util.Optional;

import com.example.domain.user.AptzipUserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AptzipUserEntity, Long> {
	
	Optional<AptzipUserEntity> findByEmail(String email);
	
	// UserResponseDto findByEmail(String email);
	
	// error
	// @Query("select u from tb_user u where u.email = :email")
	// public Optional<User> findByUsername(@Param("email") String email);
}
