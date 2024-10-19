package com.fingerchar.core.storage.helper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 存储帮助类
 *
 * @author lance
 * @since 2024/10/17 23:22
 */
@Slf4j
@UtilityClass
public class StorageHelper {

  /**
   * 文件转换MultipartFile -> file
   *
   * @param multipartFile MultipartFile
   * @param tmpDir        临时文件目录
   * @return MultipartFile
   */
  public static File convertFile(MultipartFile multipartFile, String tmpDir) {
    try {
      File file = new File(tmpDir + multipartFile.getOriginalFilename());
      multipartFile.transferTo(file);
      return file;
    } catch (IOException ex) {
      log.info("convert file[filename: {}, tmp: {}] fail", multipartFile.getName(), tmpDir);
    }
    return null;
  }
}
