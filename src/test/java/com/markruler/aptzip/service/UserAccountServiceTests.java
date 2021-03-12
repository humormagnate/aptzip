package com.markruler.aptzip.service;

import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.markruler.aptzip.domain.user.UserAccountEntity;
import com.markruler.aptzip.domain.user.UserAccountRequestDto;
import com.markruler.aptzip.domain.user.UserRole;
import com.markruler.aptzip.persistence.user.UserJpaRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceTests {

  @InjectMocks
  private UserAccountService service;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private UserJpaRepository repository;

  // @Disabled("tmp")
  @Test
  @DisplayName("회원가입시 사용자를 DB에 저장합니다")
  void testSave() {
    // given
    UserAccountRequestDto user = UserAccountRequestDto.builder().id(1L).username("changsu").password("passwd")
        .role(UserRole.USER).email("changsu@aptzip.com").isEnabled(true).build();

    // mocking
    BDDMockito.given(repository.save(any())).willReturn(user.toEntity());

    // when
    UserAccountEntity returnedUser = service.save(user, "A10024484");

    // then
    Assertions.assertNotNull(returnedUser, "The saved user should not be null");
    Assertions.assertEquals("changsu", returnedUser.getUsername(), "The id should be incremented.");
  }

  @Test
  @WithMockUser(authorities = { "ROLE_ADMIN" })
  @DisplayName("관리자 권한이 있을 경우 모든 사용자를 조회할 수 있습니다")
  void testFindAll() {
    // given
    UserAccountRequestDto user1 = UserAccountRequestDto.builder().id(1L).role(UserRole.ADMIN).build();
    UserAccountRequestDto user2 = UserAccountRequestDto.builder().id(2L).role(UserRole.USER).build();

    // mocking
    BDDMockito.given(repository.findAll()).willReturn(Arrays.asList(user1.toEntity(), user2.toEntity()));

    // when
    List<UserAccountRequestDto> returnedUsers = service.findAll();

    // then
    Assertions.assertEquals(2, returnedUsers.size(), "findAll should return 2 users");
  }

  @Test
  @DisplayName("사용자 ID로 사용자 엔터티를 찾습니다")
  void testFindById() {
    // given
    UserAccountEntity user = UserAccountEntity.builder().id(1L).email("test@aptzip.com").build();

    // mocking
    BDDMockito.given(repository.findById(1L)).willReturn(Optional.of(user));

    // when
    Optional<UserAccountEntity> returnedUser = service.findById(1L);

    // then
    Assertions.assertTrue(returnedUser.isPresent(), "User was not found");
    Assertions.assertSame(returnedUser.get(), user, "The user returned was not the same as the mock");
  }

  @Test
  @DisplayName("사용자 ID를 찾을 수 없습니다")
  void testFindByIdNotFound() {
    // mocking
    BDDMockito.given(repository.findById(1L)).willReturn(Optional.empty());

    // when
    Optional<UserAccountEntity> returnedWidget = service.findById(1L);

    // then
    Assertions.assertFalse(returnedWidget.isPresent(), "User should not be found");
  }
}
