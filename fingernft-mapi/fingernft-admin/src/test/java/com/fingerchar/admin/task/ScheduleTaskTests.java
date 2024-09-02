package com.fingerchar.admin.task;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * ScheduleTask
 *
 * @author lance
 * @since 2024/8/17 21:39
 */
@Slf4j
@SpringBootTest
class ScheduleTaskTests {
  @Autowired
  private ScheduleTask scheduleTask;

  @Test
  void syncEthLastBlock() {
    long start = System.currentTimeMillis();
    scheduleTask.syncEthLastBlock();

    log.info("===>{}", System.currentTimeMillis() - start);
  }
}