package com.fingerchar.api.web;

import com.fingerchar.api.service.FcStorageService;
import com.fingerchar.api.service.StorageService;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.common.consts.SysConfConst;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * 对象存储服务
 */
@RestController
@RequestMapping(SysConfConst.URL_PREFIX + "/storage")
public class FcStorageController extends BaseController {

  @Autowired
  private StorageService storageService;

  @Autowired
  private FcStorageService fcStorageService;


  /**
   * 上传文件
   * 1、获取到前端上传来的MultipartFile对象
   * 2、获取到源文件名称
   * 3、将文件交给service层处理
   */
  @PostMapping("/upload")
  public Object upload(@RequestParam("file") MultipartFile file) throws IOException {
    //return ResponseUtil.ok(LanceTestSource.upload());
    String originalFilename = file.getOriginalFilename();
    FcStorage fcStorage = storageService.store(file.getInputStream(), file.getSize(), file.getContentType(), originalFilename);
    if (null == fcStorage) {
      return ResponseUtil.fail();
    } else {
      return ResponseUtil.ok(fcStorage);
    }
  }

  @PostMapping("/multiupload")
  public Object multiUpload(@RequestParam("files") MultipartFile[] files) throws IOException {
    int len = files.length;
    String[] fileNames = new String[len];
    InputStream[] inputStreams = new InputStream[len];
    Long[] contentLengths = new Long[len];
    String[] contentTypes = new String[len];
    int i = 0;
    for (MultipartFile file : files) {
      fileNames[i] = file.getOriginalFilename();
      inputStreams[i] = file.getInputStream();
      contentLengths[i] = file.getSize();
      contentTypes[i] = file.getContentType();
      i++;
    }
    List<FcStorage> list = storageService.store(inputStreams, contentLengths, contentTypes, fileNames);
    if (null == list) {
      return ResponseUtil.fail();
    } else {
      return ResponseUtil.ok(list);
    }
  }

  /**
   * 访问存储对象
   *
   * @param key 存储对象key
   * @return
   */
  @PostMapping("/fetch/{key:.+}")
  public ResponseEntity<Resource> fetch(@PathVariable String key) {
    FcStorage fcStorage = fcStorageService.findByKey(key);
    if (key == null) {
      return ResponseEntity.notFound().build();
    }
    if (key.contains("../")) {
      return ResponseEntity.badRequest().build();
    }
    String type = fcStorage.getType();
    MediaType mediaType = MediaType.parseMediaType(type);

    Resource file = storageService.loadAsResource(key);
    if (file == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok().contentType(mediaType).body(file);
  }

  /**
   * 访问存储对象
   *
   * @param key 存储对象key
   * @return
   */
  @PostMapping("/download/{key:.+}")
  public ResponseEntity<Resource> download(@PathVariable String key) {
    FcStorage fcStorage = fcStorageService.findByKey(key);
    if (key == null) {
      return ResponseEntity.notFound().build();
    }
    if (key.contains("../")) {
      return ResponseEntity.badRequest().build();
    }

    String type = fcStorage.getType();
    MediaType mediaType = MediaType.parseMediaType(type);

    Resource file = storageService.loadAsResource(key);
    if (file == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok().contentType(mediaType).header(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

}
