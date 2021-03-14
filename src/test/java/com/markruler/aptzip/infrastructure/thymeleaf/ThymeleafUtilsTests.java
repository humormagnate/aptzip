package com.markruler.aptzip.infrastructure.thymeleaf;

import java.time.LocalDateTime;
import java.util.Locale;

import com.markruler.aptzip.infrastructure.thymeleaf.expression.TemporalsAptzip;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ThymeleafUtilsTests {

  private final TemporalsAptzip temporals = new TemporalsAptzip(Locale.KOREA);

  @Test
  @DisplayName("\"hour\" 값이 다르다고 \"1h\"으로 표시하지 않습니다.")
  void testBetweenNowAndTimeHour() {
    Long resultMidnight = temporals.betweenNowAndTimeHour(LocalDateTime.now().minusMinutes(59));
    Assertions.assertEquals(0, resultMidnight);
  }

  @Test
  @DisplayName("현재 시각은 \"0h\"으로 표시합니다.")
  void testBetweenNowAndTime() {
    String resultHour = temporals.betweenNowAndTime(LocalDateTime.now());
    Assertions.assertEquals("0h", resultHour);
  }

  @Test
  @DisplayName("1시간 미만인지 확인합니다.")
  void testIsLessThanOneHour() {
    LocalDateTime lessHour = LocalDateTime.now().minusMinutes(59);
    Assertions.assertTrue(temporals.isLessThanOneHour(lessHour));

    LocalDateTime moreHour = LocalDateTime.now().minusHours(1);
    Assertions.assertFalse(temporals.isLessThanOneHour(moreHour));
  }
}
