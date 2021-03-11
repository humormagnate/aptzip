package com.markruler.aptzip.persistence.user;


import java.util.List;
import java.util.Optional;
import com.markruler.aptzip.domain.apartment.AptEntity;
import com.markruler.aptzip.domain.user.AptzipRoleEntity;
import com.markruler.aptzip.domain.user.UserAccountEntity;
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
	List<UserAccountEntity> findAllByAptAndRole(AptEntity apt, AptzipRoleEntity role);

	@Modifying
	@Query("UPDATE UserAccountEntity u SET u.isEnabled = 0 WHERE u.id = :id")
	void disabledUserById(@Param("id") Long id);

	@Modifying
	@Query("UPDATE UserAccountEntity u SET u.isEnabled = 1 WHERE u.id = :id")
	void enabledUserById(@Param("id") Long id);

	@Modifying
	@Query("UPDATE UserAccountEntity u SET u.password = :password WHERE u.id = :id")
	void updatePasswordById(@Param("password") String password, @Param("id") Long id);

}
