package com.fingerchar.core.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
   */
  private long corsMaxAge = 30 * 24 * 60 * 60L;
}
