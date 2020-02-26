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
  public void testBetweenNowAndTimeHour() {
    int currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) - 1;
    LocalTime local = LocalTime.of(currentTime, 59);
    log.info(currentTime + "");
    log.info(local.toString());
    
    Long result = temporals.betweenNowAndTimeHour(LocalDateTime.of(LocalDate.now(), local));
    log.info("result : {}", result);

    assertNotNull(result);
    assertTrue(result instanceof Long);

    assertEquals(result, 1L);
  }
  
  @Test
  public void testBetweenNowAndTime() {
    // int year, int month, int dayOfMonth, int hour, int minute, int second, int nanoOfSecond
    String resultHour0 = temporals.betweenNowAndTime(LocalDateTime.of(2020, 2, 27, 3, 30, 1, 1));
    log.info("resultHour0 : {}", resultHour0);
    String resultHour1 = temporals.betweenNowAndTime(LocalDateTime.of(2020, 2, 27, 3, 5, 1, 1));
    log.info("resultHour1 : {}", resultHour1);
    String resultDay = temporals.betweenNowAndTime(LocalDateTime.of(2020, 2, 26, 4, 1, 1, 1));
    log.info("resultDay : {}", resultDay);
    String resultMonth = temporals.betweenNowAndTime(LocalDateTime.of(2020, 1, 26, 4, 1, 1, 1));
    log.info("resultMonth : {}", resultMonth);
    String resultYear = temporals.betweenNowAndTime(LocalDateTime.of(2019, 2, 25, 4, 1, 1, 1));
    log.info("resultYear : {}", resultYear);

    assertNotNull(resultHour0);
    assertTrue(resultHour0 instanceof String);

    assertEquals(resultHour0, "0h");
  }

  @Test
  public void testIsItOneHourAgo(){
    LocalDateTime time = LocalDateTime.of(LocalDate.now(), LocalTime.of(3, 4, 0));
    assertTrue(temporals.isItOneHourAgo(time));
  }
}