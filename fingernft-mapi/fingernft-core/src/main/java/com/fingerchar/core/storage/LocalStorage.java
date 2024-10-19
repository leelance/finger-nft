package com.fingerchar.core.storage;


import com.fingerchar.core.config.properties.StorageProperties;
import com.fingerchar.core.common.exception.ServiceException;
import com.fingerchar.core.util.StringConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

/**
 * 服务器本地对象存储服务
 *
 * @author admin
 * @since 2024/10/17 16:28
 */
@Slf4j
public class LocalStorage implements BaseStorage {
  private final StorageProperties.LocalProperties properties;

  private Path rootLocation;

  public LocalStorage(StorageProperties properties) {
    this.properties = properties.getLocal();

    try {
      String dir = properties.getLocal().getStoragePath();
      if (!new File(dir).exists()) {
        this.rootLocation = Paths.get(dir);
        Files.createDirectories(this.rootLocation);
      }
    } catch (Exception ex) {
      log.warn("create local storage path fail: ", ex);
    }
  }

  @Override
  public void store(InputStream inputStream, long contentLength, String contentType, String keyName) {
    try {
      Files.copy(inputStream, rootLocation.resolve(keyName), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw ServiceException.of("Failed to store file " + keyName, e);
    }
  }

  @Override
  public Stream<Path> loadAll() {
    try (Stream<Path> walk = Files.walk(rootLocation, 1)) {
      return walk.filter(path -> !path.equals(rootLocation)).map(path -> rootLocation.relativize(path));
    } catch (IOException e) {
      throw ServiceException.of("Failed to read stored files ", e);
    }
  }

  @Override
  public Path load(String filename) {
    return rootLocation.resolve(filename);
  }

  @Override
  public Resource loadAsResource(String filename) {
    try {
      Path file = load(filename);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        return null;
      }
    } catch (MalformedURLException e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  @Override
  public void delete(String filename) {
    Path file = load(filename);
    try {
      Files.delete(file);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
  }

  @Override
  public String generateUrl(String keyName) {
    return properties.getStoragePath().endsWith(StringConst.SLASH) ? properties.getStoragePath() + keyName : properties.getStoragePath() + StringConst.SLASH + keyName;
  }
}