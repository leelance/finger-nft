package com.fingerchar.db.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * BaseEntity
 *
 * @author admin
 * @since 2024/9/17 8:06
 */
@Data
public class BaseEntity {

  @TableId(type = IdType.AUTO)
  protected Long id;

  @TableField(value = "deleted")
  protected Boolean deleted;

  @TableField(value = "`create_time`", fill = FieldFill.INSERT)
  protected Long createTime;

  @TableField(value = "`update_time`", fill = FieldFill.INSERT_UPDATE)
  protected Long updateTime;

  public static final String ID = "id";

  public static final String DELETED = "deleted";

  public static final String CREATE_TIME = "create_time";

  public static final String UPDATE_TIME = "update_time";

}
