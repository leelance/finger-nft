package com.fingerchar.core.exception;

/**
 * 自定义异常类
 *
 * @author lance
 * @since 2024/8/17 20:28
 */
public abstract class AbstractBizException extends RuntimeException {
  protected AbstractBizException() {
  }

  protected AbstractBizException(String message) {
    super(message);
  }

  protected AbstractBizException(String message, Throwable cause) {
    super(message, cause);
  }

  protected AbstractBizException(Throwable cause) {
    super(cause);
  }

  protected AbstractBizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
