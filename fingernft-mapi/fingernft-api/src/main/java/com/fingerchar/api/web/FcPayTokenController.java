package com.fingerchar.api.web;

import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.common.consts.SysConfConst;
import com.fingerchar.core.manager.FcPayTokenManager;
import com.fingerchar.core.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * FcPayTokenController
 *
 * @author admin
 * @since 2024/9/17 13:32
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(SysConfConst.URL_PREFIX + "/paytoken")
public class FcPayTokenController extends BaseController {
  private final FcPayTokenManager payTokenManager;

  @PostMapping(value = "/list")
  public Object list() {
    return ResponseUtil.ok(this.payTokenManager.all());
  }
}
