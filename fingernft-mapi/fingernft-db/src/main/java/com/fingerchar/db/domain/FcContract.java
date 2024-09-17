package com.fingerchar.db.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fingerchar.db.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * FcContract
 *
 * @author admin
 * @since 2024/9/17 8:03
 */
@Data
@TableName("fc_contract")
@EqualsAndHashCode(callSuper = true)
public class FcContract extends BaseEntity {

  /**
   * nft name
   */
  @TableField("`name`")
  private String name;

  /**
   * nft symbol
   */
  @TableField("`symbol`")
  private String symbol;

  /**
   * 合约地址
   */
  @TableField("`address`")
  private String address;

  /**
   * 短地址
   */
  @TableField("`short_url`")
  private String shortUrl;

  /**
   * 合约版本
   */
  @TableField("`version`")
  private String version;

  /**
   * 图标
   */
  @TableField("`cover`")
  private String cover;

  /**
   * 图标保存Id
   */
  @TableField("`storage_id`")
  private Long storageId;

  /**
   * 合约拥有者地址
   */
  @TableField("`owner`")
  private String owner;

  /**
   * 是否是官方合约
   */
  @TableField("`is_admin`")
  private Boolean isAdmin;

  /**
   * 是否已验证
   */
  @TableField("`verify`")
  private Boolean verify;

  /**
   * 合约描述
   */
  @TableField("`description`")
  private String description;

  /**
   * 上一次增发的tokenId
   */
  @TableField("`last_token_id`")
  private Long lastTokenId;

  /**
   * 封面
   */
  @TableField("`banner_url`")
  private String bannerUrl;

  /**
   * 获取name和symbol次数
   */
  @TableField("`get_info_times`")
  private Integer getInfoTimes;

  /**
   * 是否支持版权
   */
  @TableField("`is_royalties`")
  private Boolean isRoyalties;

  /**
   * 签名人账号
   */
  @TableField("`signer`")
  private String signer;

  /**
   * 是否已经同步
   */
  @TableField("`is_sync`")
  private Boolean isSync;


  public static final String NAME = "`name`";

  public static final String SYMBOL = "`symbol`";

  public static final String ADDRESS = "`address`";

  public static final String SHORT_URL = "`short_url`";

  public static final String VERSION = "`version`";

  public static final String COVER = "`cover`";

  public static final String STORAGE_ID = "`storage_id`";

  public static final String OWNER = "`owner`";

  public static final String IS_ADMIN = "`is_admin`";

  public static final String VERIFY = "`verify`";

  public static final String DESCRIPTION = "`description`";

  public static final String LAST_TOKEN_ID = "`last_token_id`";

  public static final String BANNER_URL = "`banner_url`";

  public static final String GET_INFO_TIMES = "`get_info_times`";

  public static final String IS_ROYALTIES = "`is_royalties`";

  public static final String SIGNER = "`signer`";

  public static final String IS_SYNC = "`is_sync`";

}
