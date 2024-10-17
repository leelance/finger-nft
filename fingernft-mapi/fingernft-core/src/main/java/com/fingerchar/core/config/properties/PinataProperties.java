package com.fingerchar.core.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * pinata 配置
 *
 * @author lance
 * @since 2024/10/17 16:06
 */
@Data
@ConfigurationProperties(prefix = PinataProperties.PREFIX)
public class PinataProperties {
  public static final String PREFIX = "com.finger.storage.pinata";
  /**
   * api访问
   */
  private String apiDomain;
  /**
   * 授权token
   */
  private String accessToken;
  /**
   * 上传文件domain
   */
  private String uploadDomain;
  /**
   * 访问网关地址
   */
  private String gatewayDomain;
}
