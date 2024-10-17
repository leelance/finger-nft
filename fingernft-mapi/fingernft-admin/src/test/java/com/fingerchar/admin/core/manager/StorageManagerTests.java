package com.fingerchar.admin.core.manager;

import com.fingerchar.core.manager.StorageManager;
import com.fingerchar.db.domain.FcStorage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * 测试StorageManager
 *
 * @author lance
 * @since 2024/10/15 22:47
 */
@Slf4j
@SpringBootTest
class StorageManagerTests {
  @Autowired
  private StorageManager storageManager;

  @Test
  void store() throws IOException {
    String image = "D:\\data\\java\\static\\upload\\0mcxkycj9h74a69quoc8.jpg";
    File file = new File(image);

    InputStream is = Files.newInputStream(file.toPath());
    long contentLength = file.length();
    String contentType = MediaType.IMAGE_JPEG_VALUE;
    String fileName = file.getName();
    String flag = "ipfs";

    FcStorage storage = storageManager.store(is, contentLength, contentType, fileName, flag);
    log.info("===>{}", storage);
    Assertions.assertNotNull(storage);
  }
}
