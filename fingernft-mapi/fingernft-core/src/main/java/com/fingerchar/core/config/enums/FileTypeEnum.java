package com.fingerchar.core.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件类型
 *
 * @author lance
 * @since 2024/10/17 15:57
 */
@Getter
@AllArgsConstructor
public enum FileTypeEnum {
  PLAIN("plain", "普通文件类型"),
  JSON("json", "普通文件类型"),
  ;

  private final String type;
  private final String desc;
}
