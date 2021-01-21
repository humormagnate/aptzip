package com.markruler.aptzip.config.thymeleaf.expression;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.Temporal;
import java.util.Locale;

import com.markruler.aptzip.config.thymeleaf.util.TemporalAptzipUtils;

import org.thymeleaf.util.Validate;

public final class TemporalsAptzip {

  private final TemporalAptzipUtils temporalAptzipUtils;

  public TemporalsAptzip(final Locale locale) {
    this(locale, ZoneId.systemDefault());
  }

  public TemporalsAptzip(final Locale locale, final ZoneId defaultZoneId) {
    super();
    Validate.notNull(locale, "Locale cannot be null");
    this.temporalAptzipUtils = new TemporalAptzipUtils();
  }

  public Temporal minusOneHour() {
    return temporalAptzipUtils.minusOneHour();
  }

  public String betweenNowAndTime(LocalDateTime time) {
    return temporalAptzipUtils.betweenNowAndTime(time);
  }

  public Long betweenNowAndTimeHour(LocalDateTime time) {
    return temporalAptzipUtils.betweenNowAndTimeHour(time);
  }

  public Long betweenNowAndTimeDay(LocalDateTime time) {
    return temporalAptzipUtils.betweenNowAndTimeDay(time);
  }

  public Long betweenNowAndTimeMonth(LocalDateTime time) {
    return temporalAptzipUtils.betweenNowAndTimeMonth(time);
  }

  public Long betweenNowAndTimeYear(LocalDateTime time) {
    return temporalAptzipUtils.betweenNowAndTimeYear(time);
  }

  public boolean isItOneHourAgo(LocalDateTime time) {
    return temporalAptzipUtils.isItOneHourAgo(time);
  }

}