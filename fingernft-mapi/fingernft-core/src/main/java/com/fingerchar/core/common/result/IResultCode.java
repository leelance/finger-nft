package com.fingerchar.core.common.result;

import lombok.AllArgsConstructor;

import java.io.Serializable;

/**
 * 错误码定义接口
 *
 * @author lance
 * @since 2024/8/17 20:31
 */
public interface IResultCode extends Serializable {

  /**
   * 消息
   *
   * @return String
   */
  String getMessage();

  /**
   * 状态码
   *
   * @return int
   */
  String getCode();

  /**
   * 服务错误码前缀
   */
  @AllArgsConstructor
  enum P {
    /**
     * Rms系统
     */
    CORE("101", "core module"),
    DB("102", "db module"),
    API("103", "finger-nft-api"),
    ADMIN("104", "finger nft admin"),
    ;

    private final String prefix;
    private final String desc;

    public String prefix() {
      return this.prefix;
    }
  }
}
