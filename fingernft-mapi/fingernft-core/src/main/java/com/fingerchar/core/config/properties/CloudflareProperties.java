package com.fingerchar.core.config.properties;

import lombok.Data;

/**
 * cloudflare 配置
 *
 * @author lance
 * @since 2024/10/17 16:16
 */
@Data
public class CloudflareProperties {
  /**
   * api访问
   */
  private String apiDomain;
}
