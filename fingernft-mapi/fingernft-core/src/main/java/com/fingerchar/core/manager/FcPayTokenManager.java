package com.fingerchar.core.manager;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.common.enums.ContractType;
import com.fingerchar.core.common.consts.SysConfConst;
import com.fingerchar.core.util.DappWeb3jUtil;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcPayToken;
import com.fingerchar.db.vo.ERCTokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * FcPayTokenManager
 *
 * @author Zjm
 * @since 2024/9/17 13:33
 */
@Service
@RequiredArgsConstructor
public class FcPayTokenManager {
  private final FcSystemConfigManager systemConfigManager;
  private final IBaseService baseService;

  public List<FcPayToken> all() {
    QueryWrapper<FcPayToken> wrapper = new QueryWrapper<>();
    wrapper.eq(BaseEntity.DELETED, false);
    return this.baseService.findByCondition(FcPayToken.class, wrapper);
  }

  public FcPayToken getPayTokenInfo(FcPayToken payToken) {
    if (payToken.getType().equals(ContractType.ETH.getType())) {
      // paytoken type == mainnet coin
      String ConfigNetwork = this.systemConfigManager.getKeyValue(SysConfConst.CONFIG_NETWORK);
      payToken.setDecimals(18);
      if (ConfigNetwork.isEmpty()) {
        payToken.setName("ETH");
        payToken.setSymbol("ETH");
        payToken.setAddress(SysConfConst.ZERO_ADDRESS);
      } else {
        Map<String, Object> networkMap = JSON.parseObject(ConfigNetwork);
        String symbol = (String) networkMap.get("symbol");
        payToken.setName(symbol);
        payToken.setSymbol(symbol);
        payToken.setAddress(SysConfConst.ZERO_ADDRESS);
      }
      return payToken;
    }
    try {
      if (payToken.getType().equals(ContractType.ERC20.getType())) {
        // get erc20 name + symbol + decimals at chain
        ERCTokenInfo info = DappWeb3jUtil.getErc20Info(payToken.getAddress());
        payToken.setName(info.getContractName());
        payToken.setDecimals(info.getContractDecimals());
        payToken.setSymbol(info.getContractSymbol());
        return payToken;
      }
    } catch (Exception e) {
      return null;
    }
    return null;
  }

  public FcPayToken getETH() {
    QueryWrapper<FcPayToken> wrapper = new QueryWrapper<>();
    wrapper.eq(FcPayToken.TYPE, ContractType.ETH.getType());
    wrapper.eq(FcPayToken.DELETED, false);
    return this.baseService.getByCondition(FcPayToken.class, wrapper);
  }

  public FcPayToken getDefault() {
    QueryWrapper<FcPayToken> wrapper = new QueryWrapper<>();
    wrapper.eq(FcPayToken.IS_DEFAULT, true);
    wrapper.eq(FcPayToken.DELETED, false);
    return this.baseService.getByCondition(FcPayToken.class, wrapper);
  }

  public FcPayToken get(String address) {
    QueryWrapper<FcPayToken> wrapper = new QueryWrapper<>();
    wrapper.eq(FcPayToken.ADDRESS, address)
        .eq(FcPayToken.DELETED, false);
    return this.baseService.getByCondition(FcPayToken.class, wrapper);
  }

  public Integer delete(String address) {
    UpdateWrapper<FcPayToken> wrapper = new UpdateWrapper<>();
    wrapper.eq(FcPayToken.ADDRESS, address);
    return this.baseService.deleteByCondition(FcPayToken.class, wrapper);
  }

  public Integer update(FcPayToken payToken) {
    return this.baseService.update(payToken);
  }

  public Integer save(FcPayToken payToken) {
    return this.baseService.save(payToken);
  }
}
