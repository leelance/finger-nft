package com.fingerchar.api.init;

import com.fingerchar.api.service.StorageService;
import com.fingerchar.api.utils.DappCryptoUtil;
import com.fingerchar.api.utils.DappWeb3jUtil;
import com.fingerchar.core.config.properties.StorageProperties;
import com.fingerchar.core.common.consts.SysConfConst;
import com.fingerchar.core.manager.FcSystemConfigManager;
import com.fingerchar.core.storage.IpfsStorage;
import com.fingerchar.core.util.SpringContextUtil;
import com.fingerchar.core.util.json.JsonUtils;
import com.fingerchar.db.dto.ConfigDeploy;
import com.fingerchar.db.dto.ConfigNetwork;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Init Runner
 *
 * @author admin
 * @since 2024/8/17 19:39
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InitRunner implements CommandLineRunner {
  private final FcSystemConfigManager systemConfigManager;
  private final StorageProperties storageProperties;

  @Override
  public void run(String... args) throws Exception {
    log.info("===>init start");
    this.initKey();
    this.initIpfs();
    this.initWeb3j();
    log.info("===>init end");
  }

  private void initKey() {
    log.info("===>init sign key start");
    ConfigDeploy configDeploy = this.systemConfigManager.getConfigDeploy();
    if (null == configDeploy) {
      return;
    }
    String mintKey = configDeploy.getMinerKey();
    String buyerFeeKey = configDeploy.getBuyerFeeKey();
    DappCryptoUtil.setMintKey(mintKey);
    DappCryptoUtil.setBuyerFeeKey(buyerFeeKey);
    log.info("===>init sign key end");
  }

  private void initIpfs() {
    log.info("===>init ipfs start");
    StorageService storageService = SpringContextUtil.getBean(StorageService.class);
    IpfsStorage storage = new IpfsStorage(storageProperties);
    storageService.setStorage(storage);
    log.info("===>init ipfs end");
  }

  private void initWeb3j() {
    log.info("===>init web3j start");
    String value = this.systemConfigManager.getKeyValue(SysConfConst.CONFIG_NETWORK);
    if (value.isEmpty()) {
      return;
    }
    ConfigNetwork configNetwork = JsonUtils.parseObject(value, ConfigNetwork.class);
    if (Objects.isNull(configNetwork) || configNetwork.getRpc().isEmpty()) {
      return;
    }

    DappWeb3jUtil.initWeb3j(configNetwork.getRpc());
    log.info("===>init web3j end.");
  }
}
