package com.fingerchar.core.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件存储类型
 *
 * @author lance
 * @since 2024/10/17 15:57
 */
@Getter
@AllArgsConstructor
public enum StorageTypeEnum {
  IPFS_DEFAULT("ipfs-default", "默认类型, 自带类型"),
  PINATA("pinata", "pinata类型, https://app.pinata.cloud/"),
  CLOUDFLARE("cloudflare", "cloudflare类型, https://dash.cloudflare.com/"),
  ;

  private final String type;
  private final String desc;
}
