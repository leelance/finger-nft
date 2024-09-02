package com.fingerchar.core.config;

import com.fingerchar.core.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Set;

/**
 * GlobalExceptionHandler
 *
 * @author admin
 * @since 2024/8/18 12:21
 */
@Slf4j
@Order
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public Object badArgumentHandler(IllegalArgumentException e, HttpServletRequest request) {
    log.error("===>illegal arg exception fail[{}]: ", request.getRequestURI(), e);
    return ResponseUtil.badArgumentValue();
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public Object badArgumentHandler(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
    log.error("===>method arg type exception fail[{}]: ", request.getRequestURI(), e);
    return ResponseUtil.badArgumentValue();
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public Object badArgumentHandler(MissingServletRequestParameterException e, HttpServletRequest request) {
    log.error("===>miss servlet request params exception fail[{}]: ", request.getRequestURI(), e);
    return ResponseUtil.badArgumentValue();
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public Object badArgumentHandler(HttpMessageNotReadableException e, HttpServletRequest request) {
    log.error("===>http message readable exception fail[{}]: ", request.getRequestURI(), e);
    return ResponseUtil.badArgumentValue();
  }

  @ExceptionHandler(ValidationException.class)
  public Object badArgumentHandler(ValidationException e, HttpServletRequest request) {
    log.error("===>validate exception fail[{}]: ", request.getRequestURI(), e);
    if (e instanceof ConstraintViolationException) {
      ConstraintViolationException exs = (ConstraintViolationException) e;
      Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
      for (ConstraintViolation<?> item : violations) {
        String message = ((PathImpl) item.getPropertyPath()).getLeafNode().getName() + item.getMessage();
        return ResponseUtil.fail(402, message);
      }
    }
    return ResponseUtil.badArgumentValue();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Object handlerException(MethodArgumentNotValidException e, HttpServletRequest request) {
    log.error("===>method arg not valid exception fail[{}]: ", request.getRequestURI(), e);
    return ResponseUtil.fail(402, e.getBindingResult().getFieldError().getDefaultMessage());
  }

  @ExceptionHandler(Exception.class)
  public Object handleException(Exception ex) {
    log.error("unknown exception fail: ", ex);
    return ResponseUtil.serious();
  }
}
