package com.example;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Locale;

import com.example.config.thymeleaf.expression.TemporalsAptzip;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class ThymeleafUtilsTest {

  private final TemporalsAptzip temporals = new TemporalsAptzip(Locale.KOREA);

  @Test
  public void testMinusOneHour() {
    Temporal temporal = temporals.minusOneHour();
    assertNotNull(temporal);
    assertTrue(temporal instanceof LocalTime);

    LocalTime time = (LocalTime) temporal;
    assertEquals(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) - 1, time.getHour());
  }

  @Test
  public void testBetweenNowAndTime() {
    int currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) - 1;
    LocalTime local = LocalTime.of(currentTime, 59);
    log.info(currentTime + "");
    log.info(local.toString());

    Long result = temporals.betweenNowAndTime(LocalDateTime.of(LocalDate.now(), local));
    assertNotNull(result);
    assertTrue(result instanceof Long);

    assertEquals(result, 1L);
  }

  @Test
  public void testIsItOneHourAgo(){
    LocalDateTime time = LocalDateTime.of(LocalDate.now(), LocalTime.of(3, 4, 0));
    assertTrue(temporals.isItOneHourAgo(time));
  }
}