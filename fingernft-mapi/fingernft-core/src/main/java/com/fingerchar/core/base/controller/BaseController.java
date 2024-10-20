package com.fingerchar.core.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fingerchar.core.common.consts.SysConst;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * base controller
 *
 * @author admin
 * @since 2024/8/17 19:29
 */
public class BaseController {
  public Logger logger = LoggerFactory.getLogger(this.getClass());

  public static final String BASICPACKAGE = "com.fingerchar.admin";
  @Resource
  protected HttpServletRequest request;

  @Resource
  protected HttpSession session;

  private Long getPageNum() {
    String page = request.getParameter(SysConst.PAGE);
    if (null == page) {
      return 1L;
    } else {
      return Long.parseLong(page);
    }
  }

  private Long getPageSize() {
    String limit = request.getParameter(SysConst.LIMIT);
    if (null == limit) {
      return 10L;
    } else {
      return Long.parseLong(limit);
    }
  }

  protected <T> Page<T> getPageInfo() {
    Page<T> page = new Page<>(this.getPageNum(), this.getPageSize());
    return page;
  }

  protected boolean isAsc(String order) {
    if (StringUtils.isNotEmpty(order)) {
      if (order.equalsIgnoreCase("ASC")) {
        return true;
      }
    }
    return false;
  }


}
