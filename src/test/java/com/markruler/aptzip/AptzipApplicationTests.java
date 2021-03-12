package com.markruler.aptzip;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
  classes = {AptzipApplication.class}
)
class AptzipApplicationTests {

  @Value("${spring.profiles.active}")
  private String profile;

  @Test
  void contextLoads() throws Exception {
    Assertions.assertEquals("dev", profile);
  }

}
