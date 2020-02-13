package com.example.persistence;


import java.util.Optional;

import com.example.domain.user.AptzipUserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Entitymanager
// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpd.misc.cdi-integration

// NamedQuery
// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
@Repository
public interface UserJpaRepository extends JpaRepository<AptzipUserEntity, Long> {
	
	Optional<AptzipUserEntity> findByEmail(String email);
	
	Optional<AptzipUserEntity> findByUsername(String username);
	
	// No property updateUser found for type AptzipUserEntity!
	// Optional<AptzipUserEntity> updateUser(UserDetails user);
}
