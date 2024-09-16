package com.fingerchar.api.service;

import com.fingerchar.api.vo.ConfigFetchVo;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.manager.FcSystemConfigManager;
import com.fingerchar.core.util.json.JsonUtils;
import com.fingerchar.db.domain.FcSystem;
import com.fingerchar.db.dto.ConfigContract;
import com.fingerchar.db.dto.ConfigDeploy;
import com.fingerchar.db.dto.ConfigNetwork;
import com.fingerchar.db.dto.GasTracker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * FcSystemConfigService
 *
 * @author lance
 * @since 2024/9/16 15:28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FcSystemConfigService {
  private final FcSystemConfigManager systemConfigManager;

  /**
   * 获取系统名称和系统配置的键值对
   */
  public ConfigFetchVo allShow() {
    List<FcSystem> systemList = this.systemConfigManager.allShow();
    ConfigFetchVo fetchVo = new ConfigFetchVo();
    for (FcSystem item : systemList) {
      if (StringUtils.isEmpty(item.getKeyValue())) {
        continue;
      }
      switch (item.getKeyName()) {
        case SysConfConstant.IPFS_URL:
          fetchVo.setIpfsUrl(item.getKeyValue());
          break;
        case SysConfConstant.SELLER_FEE:
          fetchVo.setSellerFee(item.getKeyValue());
          break;
        case SysConfConstant.BUYER_FEE:
          fetchVo.setBuyerFee(item.getKeyValue());
          break;
        case SysConfConstant.CDN_URL:
          fetchVo.setCdnUrl(item.getKeyValue());
          break;
        case SysConfConstant.LOGIN_MESSAGE:
          fetchVo.setLoginMessage(item.getKeyValue());
          break;
        case SysConfConstant.WEBSITE:
          fetchVo.setWebsite(item.getKeyValue());
          break;
        case SysConfConstant.CONFIG_DEPLOY:
          ConfigDeploy configDeploy = JsonUtils.parseObject(item.getKeyValue(), ConfigDeploy.class);
          String minerAddress = configDeploy.getMinerAddress();
          fetchVo.setMiner(minerAddress);
          break;
        case SysConfConstant.CONFIG_NETWORK:
          ConfigNetwork configNetwork = JsonUtils.parseObject(item.getKeyValue(), ConfigNetwork.class);
          configNetwork.setRpc(null);
          fetchVo.setNetwork(configNetwork);
          break;
        case SysConfConstant.CONFIG_CONTRACT:
          ConfigContract configContract = JsonUtils.parseObject(item.getKeyValue(), ConfigContract.class);
          configContract.setExchangeOrdersHolderAddress(null);
          configContract.setExchangeStateAddress(null);
          configContract.setTransferProxyForDeprecatedAddress(null);
          configContract.setNft721Address(null);
          configContract.setNft1155Address(null);
          fetchVo.setContract(configContract);
          break;
        default:

      }
    }

    log.info("===>fetch system config: {}", fetchVo);
    return fetchVo;
  }


  public GasTracker gasTracker() {
    String value = this.systemConfigManager.getKeyValue(SysConfConstant.GAS_TRACKER);
    if (StringUtils.isEmpty(value)) {
      return null;
    }

    return JsonUtils.parseObject(value, GasTracker.class);
  }
}
