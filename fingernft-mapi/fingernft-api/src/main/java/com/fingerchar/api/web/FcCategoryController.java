package com.fingerchar.api.web;

import com.fingerchar.api.service.FcNftCategoryService;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.common.consts.SysConfConst;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcNftCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author admin
 * @since 2024/9/16 19:03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(SysConfConst.URL_PREFIX + "/category")
public class FcCategoryController extends BaseController {
  private final FcNftCategoryService categoryService;

  /*
   * 展示全部分类
   * */
  @PostMapping("/list")
  public Object listAllSelectiveCategory() {
    List<FcNftCategory> categories = categoryService.findAll();
    return ResponseUtil.ok(categories);
  }
}