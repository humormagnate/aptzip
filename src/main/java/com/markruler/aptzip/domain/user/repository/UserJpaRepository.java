package com.markruler.aptzip.domain.user.repository;


import java.util.List;
import java.util.Optional;

import com.markruler.aptzip.domain.apartment.model.AptEntity;
import com.markruler.aptzip.domain.user.model.UserAccountEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<UserAccountEntity, Long> {

	Optional<UserAccountEntity> findByEmailIgnoreCase(String email);

	Optional<UserAccountEntity> findByUsername(String username);

	@Query("SELECT u FROM UserAccountEntity AS u WHERE u.apt = :apt AND u.role = :role")
	List<UserAccountEntity> findAllByAptAndRole(AptEntity apt, String role);

	@Modifying
	@Query("UPDATE UserAccountEntity u SET u.enabled = 0 WHERE u.id = :id")
	void disabledUserById(@Param("id") Long id);

	@Modifying
	@Query("UPDATE UserAccountEntity u SET u.enabled = 1 WHERE u.id = :id")
	void enabledUserById(@Param("id") Long id);

	@Modifying
	@Query("UPDATE UserAccountEntity u SET u.password = :password WHERE u.id = :id")
	void updatePasswordById(@Param("password") String password, @Param("id") Long id);

}
