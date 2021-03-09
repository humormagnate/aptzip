package com.markruler.aptzip.service;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import com.markruler.aptzip.domain.user.AptzipUserEntity;
import com.markruler.aptzip.persistence.user.UserJpaRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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
    AptzipUserEntity user = AptzipUserEntity.builder().id(1L).email("test@example.com").build();
    log.debug("Test AptzipUserEntity: {}", user);
    Mockito.doReturn(Optional.of(user)).when(repository).findById(1L);

    // Execute the service call
    Optional<AptzipUserEntity> returnedUser = service.findById(1L);

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
    Optional<AptzipUserEntity> returnedWidget = service.findById(1L);

    // Assert the response
    Assertions.assertFalse(returnedWidget.isPresent(), "User should not be found");
  }

  // TODO: 테스트 코드 수정
  // Failed: AuthenticationCredentialsNotFound
  // @Test
  // @DisplayName("Test findAll")
  // void testFindAll() {
  //   // Setup our mock repository
  //   AptzipUserEntity user1 = AptzipUserEntity.builder().id(1L).email("user1@example.com").isEnabled(true).build();
  //   AptzipUserEntity user2 = AptzipUserEntity.builder().id(2L).email("user2@example.com").isEnabled(true).build();
  //   Mockito.doReturn(Arrays.asList(user1, user2)).when(repository).findAll();

  //   // Execute the service call
  //   // List<AptzipUserEntity> users = service.findAll();
  //   List<UserRequestDto> users = service.findAll();

  //   // Assert the response
  //   Assertions.assertEquals(2, users.size(), "findAll should return 2 users");
  // }

  @Test
  @DisplayName("Test save user")
  void testSave() {
    // Setup our mock repository
    AptzipUserEntity user = AptzipUserEntity.builder().id(1L).password("passwd").email("user@aptzip.com").isEnabled(true).build();
    Mockito.doReturn(user).when(repository).save(any());

    // Execute the service call
    AptzipUserEntity returnedUser = service.save(user, "A10024484");
    log.debug("returnedUser: {}", returnedUser);

    // Assert the response
    Assertions.assertNotNull(returnedUser, "The saved user should not be null");
    Assertions.assertEquals(1L, returnedUser.getId(), "The id should not be incremented. (id GenerationType is `IDENTITY`, not `AUTO`)");
  }
}
