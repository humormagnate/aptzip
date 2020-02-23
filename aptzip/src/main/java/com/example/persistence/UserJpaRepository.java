package com.example.persistence;


import java.util.List;
import java.util.Optional;

import com.example.domain.common.AptEntity;
import com.example.domain.user.AptzipRoleEntity;
import com.example.domain.user.AptzipUserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// Entitymanager
// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpd.misc.cdi-integration

// NamedQuery
// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
@Repository
public interface UserJpaRepository extends JpaRepository<AptzipUserEntity, Long> {
	
	Optional<AptzipUserEntity> findByEmail(String email);
	
	Optional<AptzipUserEntity> findByUsername(String username);

	@Query("SELECT u FROM AptzipUserEntity AS u WHERE u.apt = :apt AND u.role = :role")
	List<AptzipUserEntity> findAllByAptAndRole(AptEntity apt, AptzipRoleEntity role);

	@Modifying
	@Query("UPDATE AptzipUserEntity u SET u.password = :password WHERE u.id = :id")
	void updatePasswordById(@Param("password") String password, @Param("id") Long id);
	
	// No property updateUser found for type AptzipUserEntity!
	// Optional<AptzipUserEntity> updateUser(UserDetails user);
}
