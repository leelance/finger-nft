package com.fingerchar.core.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletResponse;

/**
 * 定义基本的错误码, 参考HttpServlet
 *
 * @author lance
 * @since 2024/8/17 20:32
 */
@Getter
@AllArgsConstructor
public enum ResultCode implements IResultCode {

  /**
   * 操作成功
   */
  SUCCESS(HttpServletResponse.SC_OK + "", "操作成功", "Success"),

  /**
   * 业务异常
   */
  FAILURE(HttpServletResponse.SC_BAD_REQUEST + "", "网络不稳定,请稍后重试", "Failure"),

  /**
   * 请求未授权
   */
  UN_AUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED + "", "您没有权限进行该操作", "unauthorized"),

  /**
   * 404 没找到请求
   */
  NOT_FOUND(HttpServletResponse.SC_NOT_FOUND + "", "未找到您访问的页面", "Not found"),

  /**
   * 不支持当前请求方法
   */
  METHOD_NOT_SUPPORTED(HttpServletResponse.SC_METHOD_NOT_ALLOWED + "", "暂不支持您的访问", "Not supported"),

  /**
   * 不支持当前媒体类型
   */
  MEDIA_TYPE_NOT_SUPPORTED(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE + "", "暂不支持您的访问", "Media type not supported"),

  /**
   * 请求被拒绝
   */
  REQ_REJECT(HttpServletResponse.SC_FORBIDDEN + "", "您没有权限进行该操作", "Request forbidden"),

  /**
   * 服务器异常
   */
  INTERNAL_SERVER_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR + "", "数据操作错误, 请联系管理员", "Operation fail, please contact the admin"),

  /**
   * 数据重复保存
   */
  DUPLICATE_KEY_ERROR("600", "请勿重复操作", "Data duplicate"),
  /**
   * biz error define
   */
  WEB3J_CFG_NIL(P.CORE.prefix() + 1000, "web3j config is null", ""),
  WEB3J_GET_LAST_BLOCK_ERR(P.CORE.prefix() + 1001, "web3j get block number fail", ""),
  WEB3J_GET_ETH_LOG_ERR(P.CORE.prefix() + 1002, "web3j get eth log fail", ""),
  SYS_CFG_LAST_BLK_ERR(P.CORE.prefix() + 1003, "system config last block null", ""),
  WEB3J_GET_BLK_LIST_ERR(P.CORE.prefix() + 1004, "web3j batch get block list fail", ""),
  UPLOAD_FILE_ERR(P.CORE.prefix() + 1005, "upload file fail", ""),
  ;

  /**
   * code编码
   */
  final String code;
  /**
   * 中文信息描述
   */
  final String message;
  /**
   * 英文信息
   *
   * @since 2021.10.24
   */
  final String messageEn;
}
