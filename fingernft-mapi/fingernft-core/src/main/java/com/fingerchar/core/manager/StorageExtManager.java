package com.fingerchar.core.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fingerchar.core.common.consts.SysConfConst;
import com.fingerchar.core.storage.BaseStorage;
import com.fingerchar.core.util.json.JsonUtils;
import com.fingerchar.db.domain.FcStorage;
import com.fingerchar.db.dto.NftMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * StorageManager
 *
 * @author lance
 * @since 2024/10/19 16:20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StorageExtManager {
  private final BaseStorage baseStorage;
  private final FcStorageManager fcStorageManager;
  private final FcSystemConfigManager fcSystemConfigManager;

  /**
   * 上传json内容到ipfs
   *
   * @param nft NftMetadata
   * @return NftMetadata
   */
  public NftMetadata uploadMetadata(NftMetadata nft) {
    Map<String, Object> map = new HashMap<>();
    map.put("name", nft.getName());
    map.put("description", nft.getDescription());
    FcStorage storage = fcStorageManager.get(nft.getStorageId());
    map.put("image", storage.getUrl());

    if (StringUtils.isEmpty(nft.getAnimUrl())) {
      map.put("animation_url", "");
    } else {
      storage = fcStorageManager.get(nft.getAnimStorageId());
      map.put("animation_url", storage.getUrl());
    }

    String website = fcSystemConfigManager.getKeyValue(SysConfConst.WEBSITE);
    if (!StringUtils.isEmpty(nft.getTokenId())) {
      map.put("external_url", website + "/detail/" + nft.getAddress() + ":" + nft.getTokenId());
    }
    if (StringUtils.isEmpty(nft.getProperties())) {
      map.put("attributes", new ArrayList<>());
    } else {
      JSONArray ja = JSON.parseArray(nft.getProperties());
      map.put("attributes", ja);
    }

    String metadata = JsonUtils.toJsonString(map);
    FcStorage ipfsStorage = baseStorage.uploadJson2Ipfs(nft.getName() + ".json", map);
    nft.setMetadataUrl(ipfsStorage.getUrl());
    nft.setMetadataContent(metadata);
    return nft;
  }

  /**
   * 存储文件
   *
   * @param file     MultipartFile
   * @param fileName fileName
   * @return FcStorage
   */
  public FcStorage store(MultipartFile file, String fileName) {
    FcStorage storageInfo = baseStorage.uploadFile2Ipfs(file);
    if (storageInfo == null) {
      return null;
    }

    storageInfo.setName(fileName);
    storageInfo.setKey(fileName);
    storageInfo.setCreateTime(System.currentTimeMillis() / 1000);
    storageInfo.setUpdateTime(System.currentTimeMillis() / 1000);
    fcStorageManager.add(storageInfo);
    return storageInfo;
  }

  public List<FcStorage> store(MultipartFile[] files, String[] fileNames) {
    List<FcStorage> list = new ArrayList<>();
    for (int i = 0; i < files.length; i++) {
      FcStorage storage = store(files[i], fileNames[i]);
      if (storage != null) {
        list.add(storage);
      }
    }

    if (!list.isEmpty()) {
      fcStorageManager.batchSave(list);
    }
    return list;
  }

  public void delete(String keyName) {
    baseStorage.delete(keyName);
  }
}
