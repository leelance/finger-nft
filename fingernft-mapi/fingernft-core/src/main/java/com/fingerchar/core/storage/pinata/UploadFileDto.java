package com.fingerchar.core.storage.pinata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 上传文件返回对象
 *
 * @author lance
 * @since 2024/10/17 18:37
 */
@Data
public class UploadFileDto {
  private ResBody data;

  @Data
  public static class ResBody {
    /**
     * 注解
     */
    private String id;
    /**
     * 文件名称
     */
    private String name;
    /**
     * cid
     */
    private String cid;
    /**
     * 创建日期
     */
    @JsonProperty("created_at")
    private String createdAt;
    /**
     * 文件大小
     */
    private Integer size;
    /**
     * 文件数量
     */
    @JsonProperty("number_of_files")
    private long numberOfFiles;
    /**
     * 文件类型
     */
    @JsonProperty("mime_type")
    private String mimeType;
    /**
     * 操作人id
     */
    @JsonProperty("user_id")
    private String userId;
  }
}
