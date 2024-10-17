package com.fingerchar.core.storage;

import com.fingerchar.core.config.properties.StorageProperties;
import com.fingerchar.core.exception.ServiceException;
import com.fingerchar.core.result.ResultCode;
import com.fingerchar.core.storage.pinata.PinataHelper;
import com.fingerchar.core.storage.pinata.UploadFileDto;
import com.fingerchar.db.domain.FcStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Pinata
 *
 * @author lance
 * @since 2024/10/17 18:12
 */
@Slf4j
@RequiredArgsConstructor
public class PinataStorage implements BaseStorage {
  private final StorageProperties properties;
  private final RestTemplate restTemplate;

  /**
   * 上传单个文件
   *
   * @param multipartFile MultipartFile
   * @return FcStorage
   */
  @Override
  public FcStorage upload(MultipartFile multipartFile) {
    try {
      MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
      bodyBuilder.part("name", multipartFile.getName());
      bodyBuilder.part("file", new InputStreamResource(multipartFile.getInputStream()), MediaType.TEXT_PLAIN);
      MultiValueMap<String, HttpEntity<?>> body = bodyBuilder.build();

      HttpEntity<MultiValueMap<String, HttpEntity<?>>> entity = new HttpEntity<>(body, commonHeaders(MediaType.MULTIPART_FORM_DATA_VALUE));

      ResponseEntity<UploadFileDto> response = restTemplate.exchange(properties.getPinata().getUploadDomain(), HttpMethod.POST, entity, UploadFileDto.class);
      if (response.getStatusCode() != HttpStatus.OK) {
        log.info("===>pinata upload file fail, code: {}, body: {}", response.getStatusCodeValue(), response.getBody());
        throw ServiceException.of(ResultCode.UPLOAD_FILE_ERR);
      }
      return PinataHelper.upload2Storage(response.getBody(), properties);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 创建请求header
   *
   * @param contentType contentType
   * @return HttpHeaders
   */
  private HttpHeaders commonHeaders(String contentType) {
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + properties.getPinata().getAccessToken());
    headers.set(HttpHeaders.USER_AGENT, "Application");
    if (StringUtils.isNotBlank(contentType)) {
      headers.set(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE);
    }
    return headers;
  }
}
