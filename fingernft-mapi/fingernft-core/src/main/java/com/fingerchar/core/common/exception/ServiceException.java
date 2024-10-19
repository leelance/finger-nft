package com.fingerchar.core.common.exception;

import com.fingerchar.core.common.result.IResultCode;
import lombok.Getter;

/**
 * 自定义service处理异常
 *
 * @author lance
 * @since 2024/8/17 20:28
 */
@Getter
public final class ServiceException extends AbstractBizException {
  private static final long serialVersionUID = 7830050291958354682L;

  private static final String DEFAULT_FAIL_CODE = "-1";
  private static final String DEFAULT_MESSAGE = "failure";

  private final String code;

  private ServiceException(String code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  public static ServiceException of(String code) {
    return new ServiceException(code, DEFAULT_MESSAGE, null);
  }

  public static ServiceException of(Throwable cause) {
    return new ServiceException(DEFAULT_FAIL_CODE, DEFAULT_MESSAGE, cause);
  }

  public static ServiceException of(String message, Throwable cause) {
    return new ServiceException(DEFAULT_FAIL_CODE, message, cause);
  }

  public static ServiceException of(String code, String message) {
    return new ServiceException(code, message, null);
  }

  public static ServiceException of(String code, String message, Throwable cause) {
    return new ServiceException(code, message, cause);
  }

  public static ServiceException of(IResultCode errorCode) {
    return new ServiceException(errorCode.getCode(), errorCode.getMessage(), null);
  }
}
