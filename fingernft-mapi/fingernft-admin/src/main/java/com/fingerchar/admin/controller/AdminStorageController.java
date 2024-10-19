package com.fingerchar.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.admin.annotation.RequiresPermissionsDesc;
import com.fingerchar.admin.service.FcStorageService;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.manager.StorageExtManager;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcStorage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * AdminStorageController
 *
 * @author admin
 * @since 2024/9/17 0:37
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/storage")
public class AdminStorageController extends BaseController {
  private final StorageExtManager storageExtManager;
  private final FcStorageService adminStorageService;

  @RequiresPermissions("admin:storage:list")
  @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "查询")
  @PostMapping("/list")
  public Object list(String key, String name, String sort, String order) {
    IPage<FcStorage> iPage = adminStorageService.querySelective(key, name, this.getPageInfo(), this.isAsc(order), sort);
    return ResponseUtil.okList(iPage);
  }

  @RequiresPermissions("admin:storage:create")
  @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "上传")
  @RequiresAuthentication
  @PostMapping("/create")
  public Object create(@RequestParam("file") MultipartFile file) {
    String originalFilename = file.getOriginalFilename();
    FcStorage fcAdminStorage = storageExtManager.store(file, originalFilename);
    return ResponseUtil.ok(fcAdminStorage);
  }

  @RequiresPermissions("admin:storage:multiupload")
  @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "批量上传")
  @PostMapping("/multiupload")
  public Object multiUpload(@RequestParam("files") MultipartFile[] files) throws IOException {
    int len = files.length;
    String[] fileNames = new String[len];
    int i = 0;
    for (MultipartFile file : files) {
      fileNames[i] = file.getOriginalFilename();
      i++;
    }
    List<FcStorage> list = storageExtManager.store(files, fileNames);
    if (null == list || list.isEmpty()) {
      return ResponseUtil.fail();
    } else {
      return ResponseUtil.ok(list);
    }
  }

  @RequiresPermissions("admin:storage:read")
  @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "详情")
  @RequiresAuthentication
  @PostMapping("/read")
  public Object read(@NotNull Long id) {
    FcStorage storageInfo = adminStorageService.findById(id);
    if (storageInfo == null) {
      return ResponseUtil.badArgumentValue();
    }
    return ResponseUtil.ok(storageInfo);
  }

  @RequiresPermissions("admin:storage:update")
  @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "编辑")
  @RequiresAuthentication
  @PostMapping("/update")
  public Object update(FcStorage fcAdminStorage) {
    if (adminStorageService.update(fcAdminStorage) == 0) {
      return ResponseUtil.updatedDataFailed();
    }
    return ResponseUtil.ok(fcAdminStorage);
  }

  @RequiresPermissions("admin:storage:delete")
  @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "删除")
  @PostMapping("/delete")
  public Object delete(@RequestBody FcStorage fcAdminStorage) {
    String key = fcAdminStorage.getKey();
    if (StringUtils.isEmpty(key)) {
      return ResponseUtil.badArgument();
    }
    adminStorageService.deleteByKey(key);
    return ResponseUtil.ok();
  }
}
