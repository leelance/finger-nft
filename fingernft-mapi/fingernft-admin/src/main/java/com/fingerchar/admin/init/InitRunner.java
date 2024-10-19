package com.fingerchar.admin.init;

import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.manager.FcSystemConfigManager;
import com.fingerchar.core.util.DappWeb3jUtil;
import com.fingerchar.core.util.json.JsonUtils;
import com.fingerchar.db.dto.ConfigNetwork;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * admin init runner
 *
 * @author admin
 * @since 2024/8/17 20:13
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InitRunner implements CommandLineRunner {
  private final FcSystemConfigManager systemConfigManager;

  @Override
  public void run(String... args) throws Exception {
    initWeb3j();
  }

  private void initWeb3j() {
    log.info("===>init web3j start");
    String value = this.systemConfigManager.getKeyValue(SysConfConstant.CONFIG_NETWORK);
    if (null == value || value.isEmpty()) {
      return;
    }
    ConfigNetwork configNetwork = JsonUtils.parseObject(value, ConfigNetwork.class);
    if (Objects.isNull(configNetwork) || null == configNetwork.getRpc() || configNetwork.getRpc().isEmpty()) {
      return;
    }

    DappWeb3jUtil.initWeb3j(configNetwork.getRpc());
    log.info("===>init web3j end");
  }
}
