package com.markruler.aptzip.service;

import com.markruler.aptzip.domain.user.ConfirmationToken;
import com.markruler.aptzip.domain.user.UserAccountEntity;
import com.markruler.aptzip.persistence.user.ConfirmationTokenRepository;

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
class ConfirmationServiceTests {
  @Autowired
  private ConfirmationService service;

  @MockBean
  private ConfirmationTokenRepository repository;

  @Test
  @DisplayName("Test findById Success")
  void testFindById() {
    // Setup our mock repository
    UserAccountEntity user = UserAccountEntity.builder().id(1L).email("test@aptzip.com").build();
    ConfirmationToken confirmationToken = new ConfirmationToken(user);
    log.debug("Test ConfirmationToken: {}", confirmationToken);
    Mockito.doReturn(confirmationToken).when(repository).findByConfirmationToken(confirmationToken.getConfirmationToken());

    // Execute the service call
    ConfirmationToken returnedToken = service.findToken(confirmationToken.getConfirmationToken());

    // Assert the response
    Assertions.assertSame(returnedToken.getConfirmationToken(), confirmationToken.getConfirmationToken(), "The token returned was not the same as the mock");
  }
}
