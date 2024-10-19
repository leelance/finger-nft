package com.fingerchar.core.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.core.common.consts.SysConst;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * response utils
 *
 * @author admin
 * @since 2024/8/18 12:19
 */
@UtilityClass
public class ResponseUtil {
  public static Object ok() {
    Map<String, Object> obj = new HashMap<>();
    obj.put(SysConst.RESULT_ERRNO, 0);
    obj.put(SysConst.RESULT_ERRMSG, SysConst.RESULT_MSG_SUCCESS);
    return obj;
  }

  public static Object ok(Object data) {
    Map<String, Object> obj = new HashMap<>();
    obj.put(SysConst.RESULT_ERRNO, 0);
    obj.put(SysConst.RESULT_ERRMSG, SysConst.RESULT_MSG_SUCCESS);
    obj.put(SysConst.RESULT_DATA, data);
    return obj;
  }

  public static Object okEmptyPage(Integer page, Integer limit) {
    Map<String, Object> data = new HashMap<>();
    data.put(SysConst.RESULT_TOTAL, 0);
    data.put(SysConst.RESULT_PAGE, page);
    data.put(SysConst.RESULT_LIMIT, limit);
    data.put(SysConst.RESULT_PAGES, 0);
    data.put(SysConst.RESULT_LIST, new ArrayList<>());
    return ok(data);
  }

  public static Object okList(IPage<?> page) {
    Map<String, Object> data = new HashMap<>();
    if (null == page) {
      data.put(SysConst.RESULT_LIST, new ArrayList<>());
      data.put(SysConst.RESULT_TOTAL, 0);
      data.put(SysConst.RESULT_PAGE, 1);
      data.put(SysConst.RESULT_LIMIT, 10);
      data.put(SysConst.RESULT_PAGES, 0);
    } else {
      data.put(SysConst.RESULT_LIST, page.getRecords());
      data.put(SysConst.RESULT_TOTAL, page.getTotal());
      data.put(SysConst.RESULT_PAGE, page.getCurrent());
      data.put(SysConst.RESULT_LIMIT, page.getSize());
      data.put(SysConst.RESULT_PAGES, page.getPages());
    }
    return ok(data);
  }

  public static Object okList(List<?> list) {
    Map<String, Object> data = new HashMap<>();
    data.put(SysConst.RESULT_LIST, list);
    return ok(data);
  }

  public static Object okListWithoutErrMsg(List<?> list, IPage<?> pageList) {
    Map<String, Object> data = new HashMap<>();
    data.put(SysConst.RESULT_LIST, list);

    if (null != pageList) {
      data.put(SysConst.RESULT_TOTAL, pageList.getTotal());
      data.put(SysConst.RESULT_PAGE, pageList.getCurrent());
      data.put(SysConst.RESULT_LIMIT, pageList.getSize());
      data.put(SysConst.RESULT_PAGES, pageList.getPages());
    } else {
      data.put(SysConst.RESULT_TOTAL, list.size());
      data.put(SysConst.RESULT_PAGE, 1);
      data.put(SysConst.RESULT_LIMIT, list.size());
      data.put(SysConst.RESULT_PAGES, 1);
    }

    return ok(data);
  }

  public static Object okList(IPage<?> page, Object ob) {
    Map<String, Object> data = new HashMap<>();
    if (null == page) {
      data.put(SysConst.RESULT_LIST, new ArrayList<>());
      data.put(SysConst.RESULT_TOTAL, 0);
      data.put(SysConst.RESULT_PAGE, 1);
      data.put(SysConst.RESULT_LIMIT, 10);
      data.put(SysConst.RESULT_PAGES, 0);
      data.put(SysConst.RESULT_DATA, ob);
    } else {
      data.put(SysConst.RESULT_LIST, page.getRecords());
      data.put(SysConst.RESULT_TOTAL, page.getTotal());
      data.put(SysConst.RESULT_PAGE, page.getCurrent());
      data.put(SysConst.RESULT_LIMIT, page.getSize());
      data.put(SysConst.RESULT_PAGES, page.getPages());
      data.put(SysConst.RESULT_DATA, ob);
    }
    return ok(data);
  }

  public static Object okList(List<?> list, IPage<?> pageList) {
    Map<String, Object> data = new HashMap<>();
    data.put(SysConst.RESULT_LIST, list);

    if (null != pageList) {
      data.put(SysConst.RESULT_TOTAL, pageList.getTotal());
      data.put(SysConst.RESULT_PAGE, pageList.getCurrent());
      data.put(SysConst.RESULT_LIMIT, pageList.getSize());
      data.put(SysConst.RESULT_PAGES, pageList.getPages());
    } else {
      data.put(SysConst.RESULT_TOTAL, list.size());
      data.put(SysConst.RESULT_PAGE, 1);
      data.put(SysConst.RESULT_LIMIT, list.size());
      data.put(SysConst.RESULT_PAGES, 1);
    }

    return ok(data);
  }

  public static Object fail() {
    Map<String, Object> obj = new HashMap<>();
    obj.put(SysConst.RESULT_ERRNO, -1);
    obj.put(SysConst.RESULT_ERRMSG, SysConst.RESULT_MSG_ERROR);
    return obj;
  }

  public static Object fail(Map<String, String> map) {
    Map<String, Object> obj = new HashMap<>();
    obj.put(SysConst.RESULT_ERRNO, -1);
    obj.put(SysConst.RESULT_DATA, map);
    return obj;
  }

  public static Object fail(int errno, String errmsg) {
    Map<String, Object> obj = new HashMap<>();
    obj.put(SysConst.RESULT_ERRNO, errno);
    obj.put(SysConst.RESULT_ERRMSG, errmsg);
    return obj;
  }

  public static Object fail(int errno, String errmsg, String data) {
    Map<String, Object> obj = new HashMap<String, Object>(3);
    obj.put(SysConst.RESULT_ERRNO, errno);
    obj.put(SysConst.RESULT_ERRMSG, errmsg);
    obj.put(SysConst.RESULT_DATA, data);
    return obj;
  }

  public static Object badArgument() {
    return fail(401, SysConst.RESULT_MSG_BAD_ARGUMENT);
  }

  public static Object badArgumentValue() {
    return fail(402, SysConst.RESULT_MSG_BAD_ARGUMENT_VALUE);
  }

  public static Object unlogin() {
    return fail(501, SysConst.RESULT_MSG_UNLOGIN);
  }

  public static Object serious() {
    return fail(502, SysConst.RESULT_MSG_SERIOUS);
  }

  public static Object unsupport() {
    return fail(503, SysConst.RESULT_MSG_UNSUPPORT);
  }

  public static Object updatedDateExpired() {
    return fail(504, SysConst.RESULT_MSG_UPDATE_DATE_EXPIRED);
  }

  public static Object updatedDataFailed() {
    return fail(505, SysConst.RESULT_MSG_UPDATE_DATA_FAILED);
  }

  public static Object unauthz() {
    return fail(506, SysConst.RESULT_MSG_UNAUTHZ);
  }

  public static Object NotFoud() {
    return fail(507, SysConst.RESULT_MSG_NOT_FOUND);
  }
}

