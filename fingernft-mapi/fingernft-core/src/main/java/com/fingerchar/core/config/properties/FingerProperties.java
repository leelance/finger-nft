package com.fingerchar.core.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * finger 配置
 *
 * @author lance
 * @since 2024/9/16 16:25
 */
@Data
@ConfigurationProperties(prefix = FingerProperties.PREFIX)
public class FingerProperties {
  public static final String PREFIX = "com.finger";

  /**
   * 配置jwt secret
   */
  private String tokenSecret = "Finger-Nft-Token";
  /**
   * 跨域请求最大时间 30 days
   * ns for nanoseconds
   * us for microseconds
   * ms for milliseconds
   * s for seconds
   * m for minutes
   * h for hours
   * d for days
   */
  private Duration corsMaxAge = Duration.ofDays(30);
}
