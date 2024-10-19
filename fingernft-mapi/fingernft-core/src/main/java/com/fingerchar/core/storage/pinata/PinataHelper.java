package com.fingerchar.core.storage.pinata;

import com.fingerchar.core.config.properties.PinataProperties;
import com.fingerchar.core.util.StringConst;
import com.fingerchar.db.domain.FcStorage;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.net.URLConnection;
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
  public static FcStorage upload2Storage(UploadFileDto dto, PinataProperties properties) {
    if (Objects.isNull(dto)) {
      return null;
    }

    FcStorage storage = new FcStorage();
    storage.setExtId(dto.getData().getId());
    storage.setName(dto.getData().getName());
    storage.setSize(dto.getData().getSize());
    storage.setType(dto.getData().getMimeType());
    storage.setUrl(properties.getGatewayDomain() + "files/" + dto.getData().getCid());
    return storage;
  }

  /**
   * 上传ipfs file 转  FcStorage
   *
   * @param dto         PinFileToIpfsDto
   * @param properties  PinataProperties
   * @param filename    filename
   * @param newFilename 新文件名
   * @return FcStorage
   */
  public static FcStorage pinFileToIpfs2Storage(PinFileToIpfsDto dto, PinataProperties properties, String filename, String newFilename) {
    if (Objects.isNull(dto)) {
      return null;
    }

    FcStorage storage = new FcStorage();
    storage.setExtId(StringConst.EMPTY);
    storage.setName(filename);
    storage.setKey(newFilename);
    storage.setSize(dto.getPinSize());
    storage.setType(getMimeTypeFromFilename(filename));
    storage.setUrl(properties.getGatewayDomain() + "ipfs/" + dto.getIpfsHash());
    storage.setIpfsHash("ipfs://ipfs/" + dto.getIpfsHash());
    return storage;
  }

  public static String getMimeTypeFromFilename(String filename) {
    return URLConnection.guessContentTypeFromName(filename);
  }
}
