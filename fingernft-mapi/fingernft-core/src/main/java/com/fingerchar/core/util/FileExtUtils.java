package com.fingerchar.core.util;

import com.fingerchar.core.config.enums.FileTypeEnum;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * 文件utils扩展
 *
 * @author lance
 * @since 2024/10/19 17:47
 */
@Slf4j
@UtilityClass
public class FileExtUtils extends FileUtils {

  /**
   * 验证文件是否存在
   *
   * @param filepath 文件路径
   * @return true/false
   */
  public static boolean exist(String filepath) {
    return new File(filepath).exists();
  }

  /**
   * 创建文件以及文件夹
   *
   * @param filepath filepath
   * @return Path
   */
  public static Path createParentDir(String filepath) {
    try {
      File path = new File(filepath);
      createParentDirectories(path);
      return path.toPath();
    } catch (IOException e) {
      log.warn("===>create file[{}] fail: ", filepath, e);
    }

    return null;
  }

  /**
   * 获取文件名称, 不带后缀
   *
   * @param file File
   * @return filename
   */
  public static String getBaseName(File file) {
    return FilenameUtils.getBaseName(file.getName());
  }

  public static String rename(String filename, FileTypeEnum fileType) {
    String suffix = FilenameUtils.getExtension(filename);
    return IdUtils.nextId(fileType.getType()) + StringConst.DOT + suffix;
  }
}
