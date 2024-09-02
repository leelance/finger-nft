package com.fingerchar.core.exception;

import lombok.Getter;

/**
 * 自定义验证码相关异常
 *
 * @author lance
 * @since 2024/8/17 20:29
 */
@Getter
public class ValidateCodeException extends AbstractBizException {
  private static final String DEFAULT_FAIL_CODE = "V0001";
  private final String code;

  private ValidateCodeException(String code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  public static ValidateCodeException of(String message) {
    return new ValidateCodeException(DEFAULT_FAIL_CODE, message, null);
  }
}
