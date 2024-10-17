package com.fingerchar.core.storage;

import com.fingerchar.core.config.properties.IpfsDefaultProperties;
import com.fingerchar.core.config.properties.StorageProperties;
import com.fingerchar.core.exception.ServiceException;
import com.fingerchar.core.storage.ipfsext.IpfsExt;
import com.fingerchar.core.util.StringConst;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * ipfs 自定义安装配置 存储服务
 *
 * @author admin
 * @since 2024/10/17 16:28
 */
@Slf4j
public class IpfsStorage implements BaseStorage {
  private final Path localLocation;


  private final IpfsDefaultProperties ipfsDefault;

  public IpfsStorage(StorageProperties properties) {
    StorageProperties.LocalProperties local = properties.getLocal();
    this.ipfsDefault = properties.getIpfsDefault();
    this.localLocation = Paths.get(local.getStoragePath());
  }

  @Override
  public String store(InputStream inputStream, String fileName) {
    Path temp = localLocation.resolve(fileName);
    File localFile = temp.toFile();
    try {
      Files.copy(inputStream, temp, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw ServiceException.of("Failed to store temp file " + fileName, e);
    }

    IpfsExt ipfs = new IpfsExt(ipfsDefault.getHost(), ipfsDefault.getPort());
    NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(localFile);
    try {
      List<MerkleNode> list = ipfs.add(file);
      if (null != list && !list.isEmpty()) {
        String fileHash = list.get(0).hash.toString();
        String newName = fileHash + fileName.substring(fileName.lastIndexOf("."));
        boolean ignore = localFile.renameTo(new File(localFile.getParent() + StringConst.SLASH + newName));
        log.info("ipfs rename result: {}", ignore);
        //持久化
        if (StringUtils.isNotBlank(ipfsDefault.getRemoteApi())) {
          ipfs.remotePin.add(list.get(0).hash, ipfsDefault.getRemoteApi(), newName, true);
        }
        return newName;
      }
      return null;
    } catch (IOException e) {
      throw ServiceException.of("Failed to store file to ipfs " + fileName, e);
    }
  }

  @Override
  public String[] store(InputStream[] inputStreams, String[] fileNames, String dirPath) {
    String basePath = localLocation.toFile().getPath();
    File localDir = new File(basePath + StringConst.SLASH + dirPath);

    int len = inputStreams.length;
    createTmp(localDir, len, dirPath, inputStreams, fileNames);

    IpfsExt ipfs = new IpfsExt(ipfsDefault.getHost(), ipfsDefault.getPort());
    try {
      NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(localDir);
      List<MerkleNode> list = ipfs.add(file);
      if (null != list && !list.isEmpty()) {
        String publicHash = null;
        for (MerkleNode merkleNode : list) {
          if (merkleNode.name.isPresent() && dirPath.equals(merkleNode.name.get())) {
            publicHash = merkleNode.hash.toString();
            //持久化
            if (StringUtils.isNotBlank(ipfsDefault.getRemoteApi())) {
              ipfs.remotePin.add(list.get(0).hash, ipfsDefault.getRemoteApi(), "file-" + publicHash, true);
            }
          }
        }
        boolean ignore = localDir.renameTo(new File(basePath + StringConst.SLASH + publicHash));
        log.info("===>rename file: {}", ignore);
        String[] ipfsFiles = new String[2];
        for (int i = 0; i < len; i++) {
          String newName = publicHash + StringConst.SLASH + fileNames[i];
          ipfsFiles[i] = newName;
        }
        return ipfsFiles;
      }
      return new String[0];
    } catch (IOException e) {
      throw ServiceException.of("Failed to store file to ipfs ", e);
    }
  }

  @Override
  public String generateUrl(String keyName) {
    return ipfsDefault.getRemoteApi().endsWith(StringConst.SLASH) ? ipfsDefault.getRemoteApi() + keyName : ipfsDefault.getRemoteApi() + StringConst.SLASH + keyName;
  }

  private void createTmp(File localDir, int len, String dirPath, InputStream[] inputStreams, String[] fileNames) {
    try {
      if (!localDir.exists()) {
        FileUtils.forceMkdir(localDir);
      }
      Path temp;
      for (int i = 0; i < len; i++) {
        temp = localLocation.resolve(dirPath + "/" + fileNames[i]);
        Files.copy(inputStreams[i], temp, StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (IOException e) {
      throw ServiceException.of("Failed to store local file", e);
    }
  }
}
