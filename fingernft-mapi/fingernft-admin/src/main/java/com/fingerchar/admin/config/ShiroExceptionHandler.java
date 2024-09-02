package com.fingerchar.admin.config;

import com.fingerchar.core.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * shiro exception handler
 *
 * @author admin
 * @since 2024/8/18 12:15
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class ShiroExceptionHandler {
  private final HttpServletRequest request;

  @ExceptionHandler(AuthenticationException.class)
  public Object unauthenticatedHandler(AuthenticationException e) {
    log.info("===>request path: {}, fail: ", request.getRequestURL(), e);
    return ResponseUtil.unlogin();
  }

  @ExceptionHandler(AuthorizationException.class)
  public Object unauthorizedHandler(AuthorizationException e) {
    log.info("===>request path: {}, fail: ", request.getRequestURL(), e);
    return ResponseUtil.unauthz();
  }

  /**
   * 自定义业务错误, 类型
   * ResultCode.INTERNAL_SERVER_ERROR
   *
   * @param ex ServiceException
   * @return 返回异常
   */
  @ExceptionHandler(Exception.class)
  public Object handleException(Exception ex) {
    log.error("unknown exception fail[{}]: ", request.getRequestURI(), ex);
    return ResponseUtil.fail();
  }
}
