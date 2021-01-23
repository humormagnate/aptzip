package com.markruler.aptzip.config.thymeleaf.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

// @lombok.extern.slf4j.Slf4j
public final class TemporalAptzipUtils {

  public TemporalAptzipUtils() {
    super();
  }

  public Temporal minusOneHour() {
    return LocalTime.now().minusHours(1L);
  }

  // 타임리프에서 24시간이 지날 경우 d 단위로, 30일이 지날 경우 m 단위로, 12개월이 지날 경우 y 단위로 수정
  public String betweenNowAndTime(final LocalDateTime time) {
    LocalDateTime now = LocalDateTime.now();
    Long hour = ChronoUnit.MILLIS.between(time, now) / 1000L / 60L / 60L;

    String result = "";

    if (hour < 24L) {
      result = hour + "h";
    } else if (hour < (24L * 30L)) {
      result = (hour / 24L) + "d";
      // log.debug("betweenNowAndTime result : {}", result);
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
    // 30일로 통일
    Long month = ChronoUnit.MILLIS.between(time, now) / 1000L / 60L / 60L / 24L / 30L;
    return month;
  }

  public Long betweenNowAndTimeYear(final LocalDateTime time) {
    LocalDateTime now = LocalDateTime.now();
    // 30L / 12L 나누면 360일이 됨
    Long year = ChronoUnit.MILLIS.between(time, now) / 1000L / 60L / 60L / 24L / 365L;
    return year;
  }

  public boolean isItOneHourAgo(final LocalDateTime time) {
    LocalDateTime now = LocalDateTime.now();
    return ChronoUnit.MILLIS.between(time, now) <= (1 * 1000 * 60 * 60);
  }
}
