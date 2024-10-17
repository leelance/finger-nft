package com.fingerchar.core.storage.pinata;

import com.fingerchar.core.config.properties.StorageProperties;
import com.fingerchar.db.domain.FcStorage;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Pinata helper
 *
 * @author lance
 * @since 2024/10/17 18:44
 */
@Slf4j
@UtilityClass
public class PinataHelper {

  /**
   * 上传文件转换结果
   *
   * @param dto UploadFileDto
   * @return FcStorage
   */
  public static FcStorage upload2Storage(UploadFileDto dto, StorageProperties properties) {
    if (Objects.isNull(dto)) {
      return null;
    }

    FcStorage storage = new FcStorage();
    storage.setExtId(dto.getData().getId());
    storage.setName(dto.getData().getName());
    storage.setSize(dto.getData().getSize());
    storage.setType(dto.getData().getMimeType());
    storage.setUrl(properties.getPinata().getGatewayDomain() + "files/" + dto.getData().getCid());
    return storage;
  }
}
