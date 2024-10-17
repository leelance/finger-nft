package com.fingerchar.core.storage;

import com.fingerchar.core.util.StringConst;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * 对象存储接口
 *
 * @author admin
 * @since 2024/10/17 16:23
 */
public interface BaseStorage {

  /**
   * 存储一个文件对象
   *
   * @param inputStream   文件输入流
   * @param contentLength 文件长度
   * @param contentType   文件类型
   * @param keyName       文件名
   */
  default void store(InputStream inputStream, long contentLength, String contentType, String keyName) {

  }

  default String store(InputStream inputStream, String fileName) {
    return StringConst.EMPTY;
  }

  default String[] store(InputStream[] inputStreams, String[] fileNames, String dirPath) {
    return new String[0];
  }

  default Stream<Path> loadAll() {
    return null;
  }

  default Path load(String keyName) {
    return null;
  }

  default Resource loadAsResource(String keyName) {
    return null;
  }

  default void delete(String keyName) {

  }

  default String generateUrl(String keyName) {
    return StringConst.EMPTY;
  }

  /**
   * 根据ipfs hash 获取token信息
   *
   * @param ipfsHash ipfsHash
   * @return token信息
   */
  default String getTokenInfo(String ipfsHash) {
    return StringConst.EMPTY;
  }
}