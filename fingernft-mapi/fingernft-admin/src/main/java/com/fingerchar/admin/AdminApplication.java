package com.fingerchar.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * admin application
 *
 * @author admin
 * @since 2024/8/17 19:28
 */
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = {"com.fingerchar.admin", "com.fingerchar.db", "com.fingerchar.core"})
public class AdminApplication {

  public static void main(String[] args) {
    SpringApplication.run(AdminApplication.class, args);
  }
}
