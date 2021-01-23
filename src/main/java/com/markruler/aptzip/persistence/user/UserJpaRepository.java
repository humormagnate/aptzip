package com.markruler.aptzip.persistence.user;


import java.util.List;
import java.util.Optional;

import com.markruler.aptzip.domain.common.AptEntity;
import com.markruler.aptzip.domain.user.AptzipRoleEntity;
import com.markruler.aptzip.domain.user.AptzipUserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<AptzipUserEntity, Long> {
	
	Optional<AptzipUserEntity> findByEmailIgnoreCase(String email);
	
	Optional<AptzipUserEntity> findByUsername(String username);

	@Query("SELECT u FROM AptzipUserEntity AS u WHERE u.apt = :apt AND u.role = :role")
	List<AptzipUserEntity> findAllByAptAndRole(AptEntity apt, AptzipRoleEntity role);

	@Modifying
	@Query("UPDATE AptzipUserEntity u SET u.isEnabled = 0 WHERE u.id = :id")
	void disabledUserById(@Param("id") Long id);

	@Modifying
	@Query("UPDATE AptzipUserEntity u SET u.password = :password WHERE u.id = :id")
	void updatePasswordById(@Param("password") String password, @Param("id") Long id);
	
}