package com.fingerchar.admin.core.storage.pinata;

import com.fingerchar.core.storage.BaseStorage;
import com.fingerchar.db.domain.FcStorage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

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
  void upload() throws IOException {
    String filePath = "D:\\data\\java\\static\\upload\\data.json";
    InputStream is = Files.newInputStream(Paths.get(filePath));

    MultipartFile multipartFile = new MockMultipartFile("file", is);
    FcStorage storage = baseStorage.upload(multipartFile);
    log.info("===>upload file: {}", storage);
  }
}
