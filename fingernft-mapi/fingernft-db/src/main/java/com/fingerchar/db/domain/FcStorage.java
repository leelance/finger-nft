package com.fingerchar.db.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fingerchar.db.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * FcStorage
 *
 * @author admin
 * @since 2024/9/17 13:11
 */
@Data
@TableName("fc_storage")
@EqualsAndHashCode(callSuper = true)
public class FcStorage extends BaseEntity {
  /**
   * 文件的唯一索引
   */
  @TableField("`key`")
  private String key;

  /**
   * 文件名
   */
  @TableField("`name`")
  private String name;

  /**
   * 文件类型
   */
  @TableField("`type`")
  private String type;

  /**
   * 文件大小
   */
  @TableField("`size`")
  private Integer size;

  /**
   * 文件访问链接
   */
  @TableField("`url`")
  private String url;

  /**
   * ipfs hash地址
   */
  @TableField("`ipfsHash`")
  private String ipfsHash;
  /**
   * 外部id第三方
   */
  @TableField("`ext_id`")
  private String extId;

  public static final String KEY = "`key`";

  public static final String NAME = "`name`";

  public static final String TYPE = "`type`";

  public static final String SIZE = "`size`";

  public static final String URL = "`url`";

  public static final String IPFSHASH = "`ipfsHash`";
}
