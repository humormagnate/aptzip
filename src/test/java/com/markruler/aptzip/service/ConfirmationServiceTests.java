package com.markruler.aptzip.service;

import com.markruler.aptzip.domain.user.ConfirmationToken;
import com.markruler.aptzip.domain.user.UserAccountEntity;
import com.markruler.aptzip.persistence.user.ConfirmationTokenRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// @SpringBootTest
@ExtendWith(MockitoExtension.class)
class ConfirmationServiceTests {
  Logger log = LoggerFactory.getLogger(ConfirmationServiceTests.class);

  @InjectMocks
  private ConfirmationService service;

  @Mock
  private ConfirmationTokenRepository repository;

  @Test
  @DisplayName("User ID 값으로 토큰을 조회한다")
  void testFindById() {
    // given
    UserAccountEntity user = UserAccountEntity.builder().id(1L).email("test@aptzip.com").build();
    ConfirmationToken confirmationToken = new ConfirmationToken(user);
    log.debug("Test ConfirmationToken: {}", confirmationToken);

    // mocking
    BDDMockito.given(repository.findByToken(confirmationToken.getToken())).willReturn(confirmationToken);

    // when
    ConfirmationToken returnedToken = service.findByToken(confirmationToken.getToken());

    // then
    Assertions.assertSame(returnedToken.getToken(), confirmationToken.getToken(), "토큰 값이 일치하지 않습니다");
  }
}
