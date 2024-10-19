package com.fingerchar.api.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.api.service.FcContractNftService;
import com.fingerchar.api.service.FcUserService;
import com.fingerchar.api.vo.HomeIndexParamsVO;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.common.consts.SysConfConst;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcUser;
import com.fingerchar.db.vo.NftInfoVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HomeController
 *
 * @author admin
 * @since 2024/9/16 19:04
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(SysConfConst.URL_PREFIX + "/home")
public class FcHomeController extends BaseController {
  private final FcContractNftService contractNftService;
  private final FcUserService userService;

  /**
   * 获取售卖中的nft
   */
  @PostMapping("list")
  public Object list(HomeIndexParamsVO params) {
    IPage<NftInfoVo> iPage = contractNftService.findListForIndex(this.getPageInfo(), params);
    return ResponseUtil.okList(iPage);
  }

  /**
   * 通过name搜索nft列表
   *
   * @param search fc_contract_nft表中的name字段
   */
  @PostMapping("search")
  public Object search(String search) {

    if (StringUtils.isEmpty(search)) {
      search = "";
    }
    return contractNftService.findSearch(search, this.getPageInfo());
  }

  /**
   * 根据用户名模糊查找用户
   *
   * @param search 用户名
   */
  @PostMapping("searchuser")
  public Object searchUser(String search) {
    if (search == null) {
      return ResponseUtil.okList(this.getPageInfo());
    }

    IPage<FcUser> list = this.userService.findByName(search, this.getPageInfo());
    return ResponseUtil.okList(list);
  }


}
