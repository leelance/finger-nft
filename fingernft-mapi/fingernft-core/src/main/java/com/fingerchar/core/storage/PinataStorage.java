package com.fingerchar.core.storage;

import com.fingerchar.core.config.enums.FileTypeEnum;
import com.fingerchar.core.config.properties.PinataProperties;
import com.fingerchar.core.config.properties.StorageProperties;
import com.fingerchar.core.exception.ServiceException;
import com.fingerchar.core.result.ResultCode;
import com.fingerchar.core.storage.helper.StorageHelper;
import com.fingerchar.core.storage.pinata.PinFileToIpfsDto;
import com.fingerchar.core.storage.pinata.PinataHelper;
import com.fingerchar.core.util.FileExtUtils;
import com.fingerchar.db.domain.FcStorage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Pinata
 *
 * @author lance
 * @since 2024/10/17 18:12
 */
@Slf4j
public class PinataStorage implements BaseStorage {
  private final StorageProperties.LocalProperties local;
  private final PinataProperties pinata;
  private final RestTemplate rest;

  public PinataStorage(StorageProperties properties, RestTemplate rest) {
    this.local = properties.getLocal();
    this.pinata = properties.getPinata();
    this.rest = rest;
  }

  /**
   * 上传单个文件
   *
   * @param multipartFile MultipartFile
   * @return FcStorage
   */
  @Override
  public FcStorage uploadFile2Ipfs(MultipartFile multipartFile) {
    File file = StorageHelper.convertFile(multipartFile, local.getStoragePath() + "tmp/");
    if (file == null) {
      return null;
    }

    try {
      String newFilename = FileExtUtils.rename(multipartFile.getOriginalFilename(), FileTypeEnum.PLAIN);
      Map<String, Object> pinataMetadata = new HashMap<>();
      pinataMetadata.put("name", newFilename);
      Map<String, Object> pinataOptions = new HashMap<>();
      pinataOptions.put("cidVersion", 1);

      MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
      bodyBuilder.part("file", new FileSystemResource(file), MediaType.TEXT_PLAIN);
      bodyBuilder.part("pinataMetadata", pinataMetadata);
      bodyBuilder.part("pinataOptions", pinataOptions);

      MultiValueMap<String, HttpEntity<?>> body = bodyBuilder.build();

      HttpEntity<MultiValueMap<String, HttpEntity<?>>> entity = new HttpEntity<>(body, commonHeaders(MediaType.MULTIPART_FORM_DATA_VALUE));

      ResponseEntity<PinFileToIpfsDto> response = rest.exchange(pinata.getApiDomain() + "pinning/pinFileToIPFS", HttpMethod.POST, entity, PinFileToIpfsDto.class);
      if (response.getStatusCode() != HttpStatus.OK) {
        log.info("===>pinata upload file fail, code: {}, body: {}", response.getStatusCodeValue(), response.getBody());
        throw ServiceException.of(ResultCode.UPLOAD_FILE_ERR);
      }
      return PinataHelper.pinFileToIpfs2Storage(response.getBody(), pinata, multipartFile.getOriginalFilename(), newFilename);
    } finally {
      try {
        FileUtils.forceDeleteOnExit(file);
      } catch (IOException e) {
        //ignore delete
      }
    }
  }

  @Override
  public FcStorage uploadJson2Ipfs(String jsonName, Map<String, Object> json) {
    String newFilename = FileExtUtils.rename(jsonName, FileTypeEnum.JSON);

    Map<String, Object> pinataMetadata = new HashMap<>();
    pinataMetadata.put("name", newFilename);
    Map<String, Object> pinataOptions = new HashMap<>();
    pinataOptions.put("cidVersion", 1);

    Map<String, Object> body = new HashMap<>();
    body.put("pinataMetadata", pinataMetadata);
    body.put("pinataOptions", pinataOptions);
    body.put("pinataContent", json);

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, commonHeaders(MediaType.APPLICATION_JSON_VALUE));

    ResponseEntity<PinFileToIpfsDto> response = rest.exchange(pinata.getApiDomain() + "pinning/pinJSONToIPFS ", HttpMethod.POST, entity, PinFileToIpfsDto.class);
    if (response.getStatusCode() != HttpStatus.OK) {
      log.info("===>pinata upload json fail, code: {}, body: {}", response.getStatusCodeValue(), response.getBody());
      throw ServiceException.of(ResultCode.UPLOAD_FILE_ERR);
    }
    return PinataHelper.pinFileToIpfs2Storage(response.getBody(), pinata, jsonName, newFilename);
  }

  /**
   * 创建请求header
   *
   * @param contentType contentType
   * @return HttpHeaders
   */
  private HttpHeaders commonHeaders(String contentType) {
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + pinata.getAccessToken());
    headers.set(HttpHeaders.USER_AGENT, "Application");
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));

    if (StringUtils.isNotBlank(contentType)) {
      headers.set(HttpHeaders.CONTENT_TYPE, contentType);
    }
    return headers;
  }
}
