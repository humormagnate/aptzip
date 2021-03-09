package com.markruler.aptzip.config.thymeleaf;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Locale;

import com.markruler.aptzip.config.thymeleaf.expression.TemporalsAptzip;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
// @lombok.extern.slf4j.Slf4j
class ThymeleafUtilsTests {

  private final TemporalsAptzip temporals = new TemporalsAptzip(Locale.KOREA);

  @Test
  @DisplayName("An hour ago from the current time")
  void testMinusOneHour() {
    Temporal temporal = temporals.minusOneHour();
    Assertions.assertTrue(temporal instanceof LocalTime);

    LocalTime time = (LocalTime) temporal;
    Assertions.assertEquals(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) - 1, time.getHour());
  }

  @Test
  @DisplayName("It does not show \"1h\" because the \"hour\" value is different.")
  void testBetweenNowAndTimeHour() {
    int currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) - 1;
    // TODO: 테스트 코드 수정 (59분에 테스트할 경우 Failure)
    LocalTime local = LocalTime.of(currentTime, 59);
    Long result = temporals.betweenNowAndTimeHour(LocalDateTime.of(LocalDate.now(), local));
    Assertions.assertEquals(0, result);
  }

  @Test
  @DisplayName("Show 0h if present")
  void testBetweenNowAndTime() {
    String resultHour = temporals.betweenNowAndTime(LocalDateTime.now());
    Assertions.assertEquals("0h", resultHour);
  }

  @Test
  @DisplayName("Test less than one hour")
  void testIsLessThanOneHour() {
    LocalDateTime time = LocalDateTime.of(LocalDate.now(), LocalTime.now().minusHours(1L).minusMinutes(1L));
    Assertions.assertFalse(temporals.isLessThanOneHour(time));

    time = LocalDateTime.of(LocalDate.now(), LocalTime.now().minusMinutes(55L));
    Assertions.assertTrue(temporals.isLessThanOneHour(time));
  }
}
