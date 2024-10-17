package com.fingerchar.core.config.properties;

import lombok.Data;

/**
 * 自己安装的ipfs配置
 *
 * @author lance
 * @since 2024/10/17 17:04
 */
@Data
public class IpfsDefaultProperties {
  /**
   * ip地址
   */
  private String host;
  /**
   * 端口
   */
  private int port;
  /**
   * 对外访问地址, 即gateway地址
   */
  private String remoteApi;
}
