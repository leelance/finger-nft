package com.fingerchar.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * ApiApplication
 *
 * @author admin
 * @since 2024/8/17 19:52
 */
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.fingerchar.db", "com.fingerchar.core", "com.fingerchar.api"})
public class ApiApplication {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(ApiApplication.class, args);
  }
}