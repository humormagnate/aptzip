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

  public Long betweenNowAndTime(final LocalDateTime time) {
    LocalDateTime now = LocalDateTime.now();
    Long hours = ChronoUnit.MILLIS.between(time, now) / 1000L / 60L / 60L;
    return hours;
  }

  public boolean isItOneHourAgo(final LocalDateTime time) {
    LocalDateTime now = LocalDateTime.now();
    // log.info(now + "");
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