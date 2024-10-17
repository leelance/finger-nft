package com.fingerchar.admin.core.manager;

import com.fingerchar.core.config.properties.PinataProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Pinata
 *
 * @author lance
 * @since 2024/10/16 18:53
 */
@Slf4j
@SpringBootTest
class PinataStorageTests {
  @Autowired
  private PinataProperties pinataProperties;
  @Autowired
  private RestTemplate restTemplate;

  @Test
  @Disabled("echo")
  void testAuthentication() {
    String echo = "data/testAuthentication";

    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + pinataProperties.getAccessToken());
    HttpEntity<?> entity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange(pinataProperties.getApiDomain() + echo, HttpMethod.GET, entity, String.class);
    showResult(response);
  }

  @Test
  @Disabled("detail by id")
  void detailById() {
    String detailMethod = "v3/files/01929609-4a88-72b9-bd82-f1dcef2c12ee";

    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + pinataProperties.getAccessToken());
    HttpEntity<?> entity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange(pinataProperties.getApiDomain() + detailMethod, HttpMethod.GET, entity, String.class);
    showResult(response);
  }

  @Test
  @Disabled("uploadFile")
  void upload() throws IOException {
    String filePath = "D:\\data\\java\\static\\upload\\data.json";

    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + pinataProperties.getAccessToken());
    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE);
    headers.set(HttpHeaders.USER_AGENT, "Application");

    MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
    bodyBuilder.part("name", "data-demo.json");
    bodyBuilder.part("file", new FileSystemResource(filePath), MediaType.TEXT_PLAIN);
    MultiValueMap<String, HttpEntity<?>> body = bodyBuilder.build();

    HttpEntity<MultiValueMap<String, HttpEntity<?>>> entity = new HttpEntity<>(body, headers);

    ResponseEntity<String> response = restTemplate.exchange(pinataProperties.getUploadDomain(), HttpMethod.POST, entity, String.class);
    showResult(response);
  }

  @Test
  @Disabled("pinFileToIPFS")
  void pinFileToIPFS() {
    String filePath = "D:\\data\\java\\static\\upload\\data.json";
    String pinFile2Ipfs = "pinning/pinFileToIPFS";

    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + pinataProperties.getAccessToken());
    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE);

    Map<String, Object> pinataMetadata = new HashMap<>();
    pinataMetadata.put("name", "data.json");

    Map<String, Object> pinataOptions = new HashMap<>();
    pinataOptions.put("cidVersion", 1);

    MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
    bodyBuilder.part("name", "data.json");
    bodyBuilder.part("file", new FileSystemResource(filePath), MediaType.TEXT_PLAIN);
    bodyBuilder.part("pinataMetadata", pinataMetadata);
    bodyBuilder.part("pinataOptions", pinataOptions);
    MultiValueMap<String, HttpEntity<?>> body = bodyBuilder.build();

    HttpEntity<MultiValueMap<String, HttpEntity<?>>> entity = new HttpEntity<>(body, headers);

    ResponseEntity<String> response = restTemplate.exchange(pinataProperties.getApiDomain() + pinFile2Ipfs, HttpMethod.POST, entity, String.class);
    showResult(response);
  }

  private void showResult(ResponseEntity<String> response) {
    log.info("===>code: {}", response.getStatusCodeValue());
    log.info("===>body: {}", response.getBody());
  }
}
