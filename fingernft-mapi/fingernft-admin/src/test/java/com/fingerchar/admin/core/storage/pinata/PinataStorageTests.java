package com.fingerchar.admin.core.storage.pinata;

import com.fingerchar.core.storage.BaseStorage;
import com.fingerchar.db.domain.FcStorage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * PinataStorage
 *
 * @author lance
 * @since 2024/10/17 19:03
 */
@Slf4j
@SpringBootTest
class PinataStorageTests {
  @Autowired
  private BaseStorage baseStorage;

  @Test
  @Disabled("upload file to ipfs")
  void upload() throws IOException {
    String filePath = "D:\\data\\java\\static\\upload\\0b5q9d09qecjc4bgtfqh1.jpg";
    File file = new File(filePath);
    InputStream is = Files.newInputStream(Paths.get(filePath));

    MultipartFile multipartFile = new MockMultipartFile(file.getName(), is);
    FcStorage storage = baseStorage.uploadFile2Ipfs(multipartFile);
    log.info("===>upload file: {}", storage);
  }

  @Test
  void uploadJson2Ipfs() {
    String jsonName = "demo.json";
    Map<String, Object> json = new HashMap<>();
    json.put("id", "Item." + System.currentTimeMillis());
    json.put("createTime", System.currentTimeMillis());
    json.put("author", "Jim Green");

    FcStorage storage = baseStorage.uploadJson2Ipfs(jsonName, json);
    log.info("===>upload file: {}", storage);
  }
}
