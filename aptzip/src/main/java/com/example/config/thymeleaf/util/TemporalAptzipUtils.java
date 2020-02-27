package com.example.config.thymeleaf.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class TemporalAptzipUtils {
  
  public TemporalAptzipUtils() {
    super();
  }
  
  public Temporal minusOneHour() {
    return LocalTime.now().minusHours(1L);
  }

  public String betweenNowAndTime(final LocalDateTime time) {
    LocalDateTime now = LocalDateTime.now();
    Long hour = ChronoUnit.MILLIS.between(time, now) / 1000L / 60L / 60L;

    String result = "";

    if (hour < 24L) {
      result = hour + "h";
    } else if (hour < (24L * 30L)) {
      result = (hour / 24L) + "d";
      log.info("betweenNowAndTime result : {}", result);
    } else if (hour < (24L * 365L)) {
      result = (hour / 24L / 30L) + "m";
    } else {
      result = (hour / 24L / 365L) + "y";
    }
    return result;
  }

  public Long betweenNowAndTimeHour(final LocalDateTime time) {
    LocalDateTime now = LocalDateTime.now();
    Long hour = ChronoUnit.MILLIS.between(time, now) / 1000L / 60L / 60L;
    return hour;
  }

  public Long betweenNowAndTimeDay(final LocalDateTime time) {
    LocalDateTime now = LocalDateTime.now();
    Long day = ChronoUnit.MILLIS.between(time, now) / 1000L / 60L / 60L / 24L;
    return day;
  }
  
  public Long betweenNowAndTimeMonth(final LocalDateTime time) {
    LocalDateTime now = LocalDateTime.now();
    Long month = ChronoUnit.MILLIS.between(time, now) / 1000L / 60L / 60L / 24L / 30L;  // 30일로 통일
    return month;
  }

  public Long betweenNowAndTimeYear(final LocalDateTime time) {
    LocalDateTime now = LocalDateTime.now();
    Long year = ChronoUnit.MILLIS.between(time, now) / 1000L / 60L / 60L / 24L / 365L; // 30L / 12L 하면 360일이 됨
    return year;
  }

  public boolean isItOneHourAgo(final LocalDateTime time) {
    LocalDateTime now = LocalDateTime.now();
    // log.info("now : {}", now);
    // log.info(time + "");
    // log.info(ChronoUnit.MILLIS.between(time, now) + "");

    // ChronoUnit.HOURS.between 같은 경우 '시'만 보고 계산함
    // 03시 59분 - 02시 00분 : 1시간 차이
    // 그렇다면 밀리초단위로?
    // 2020-02-18T03:58:24.086
    // Done!
    return ChronoUnit.MILLIS.between(time, now) <= (1 * 1000 * 60 * 60);
  }
}