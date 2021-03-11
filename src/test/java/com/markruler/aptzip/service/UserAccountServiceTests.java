package com.markruler.aptzip.service;

import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.markruler.aptzip.domain.user.UserAccountEntity;
import com.markruler.aptzip.domain.user.UserAccountRequestDto;
import com.markruler.aptzip.persistence.user.UserJpaRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class UserAccountServiceTests {
  /**
   * Autowire in the service we want to test
   */
  @Autowired
  private UserAccountService service;

  /**
   * Create a mock implementation of the UserJpaRepository
   */
  @MockBean
  private UserJpaRepository repository;

  @Test
  @DisplayName("Test findById Success")
  void testFindById() {
    // Setup our mock repository
    UserAccountEntity user = UserAccountEntity.builder().id(1L).email("test@aptzip.com").build();
    log.debug("Test UserAccountEntity: {}", user);
    Mockito.doReturn(Optional.of(user)).when(repository).findById(1L);

    // Execute the service call
    Optional<UserAccountEntity> returnedUser = service.findById(1L);

    // Assert the response
    Assertions.assertTrue(returnedUser.isPresent(), "User was not found");
    Assertions.assertSame(returnedUser.get(), user, "The user returned was not the same as the mock");
  }

  @Test
  @DisplayName("Test findById Not Found")
  void testFindByIdNotFound() {
    // Setup our mock repository
    Mockito.doReturn(Optional.empty()).when(repository).findById(1L);

    // Execute the service call
    Optional<UserAccountEntity> returnedWidget = service.findById(1L);

    // Assert the response
    Assertions.assertFalse(returnedWidget.isPresent(), "User should not be found");
  }

  @Test
  @WithMockUser(authorities = { "ROLE_ADMIN" })
  @DisplayName("Test findAll")
  void testFindAll() {
    // Setup our mock repository
    UserAccountRequestDto user1 = UserAccountRequestDto.builder().email("user1@aptzip.com").isEnabled(true).build();
    UserAccountRequestDto user2 = UserAccountRequestDto.builder().email("user2@aptzip.com").isEnabled(true).build();
    Mockito.doReturn(Arrays.asList(user1.toEntity(), user2.toEntity())).when(repository).findAll();

    // Execute the service call
    List<UserAccountRequestDto> users = service.findAll();

    // Assert the response
    Assertions.assertEquals(2, users.size(), "findAll should return 2 users");
  }

  @Test
  @DisplayName("Test save user")
  void testSave() {
    // Setup our mock repository
    UserAccountRequestDto user = UserAccountRequestDto.builder().username("test").password("passwd")
        .email("user@aptzip.com").isEnabled(true).build();
    Mockito.doReturn(user.toEntity()).when(repository).save(any());

    UserAccountEntity returnedUser = service.save(user, "A10024484");
    log.debug("returned User: {}", returnedUser);

    // Assert the response
    Assertions.assertNotNull(returnedUser, "The saved user should not be null");
    Assertions.assertEquals("test", returnedUser.getUsername(), "The id should be incremented.");
  }
}
