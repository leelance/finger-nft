package com.fingerchar.core.config.properties;

import com.fingerchar.core.util.StringConst;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * 存储类配置
 *
 * @author lance
 * @since 2024/10/17 16:54
 */
@Data
@ConfigurationProperties(prefix = StorageProperties.PREFIX)
public class StorageProperties {
  public static final String PREFIX = "com.finger.storage";
  /**
   * 本地访问配置
   */
  private LocalProperties local = new LocalProperties();

  /**
   * ipfs 自定义安装配置
   */
  @NestedConfigurationProperty
  private IpfsDefaultProperties ipfsDefault = new IpfsDefaultProperties();

  /**
   * pinata 配置
   */
  @NestedConfigurationProperty
  private PinataProperties pinata = new PinataProperties();

  /**
   * cloudflare 配置
   */
  @NestedConfigurationProperty
  private CloudflareProperties cloudflare = new CloudflareProperties();

  @Data
  public static class LocalProperties {
    /**
     * 文件存储位置
     */
    private String storagePath;
    /**
     * 访问访问路径层级,
     * <a href="https://127.0.0.1/apiDomain/333.json">文件访问路径中间段</a>
     */
    private String apiDomain;

    public String getStoragePath() {
      if (StringUtils.isNotBlank(storagePath) && !storagePath.endsWith(StringConst.SLASH)) {
        storagePath = storagePath + StringConst.SLASH;
      }
      return storagePath;
    }
  }
}
