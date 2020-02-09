package com.example.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.example.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByEmail(String email);
	
	// UserResponseDto findByEmail(String email);
	
	// error
	// @Query("select u from tb_user u where u.email = :email")
	// public Optional<User> findByUsername(@Param("email") String email);
}
