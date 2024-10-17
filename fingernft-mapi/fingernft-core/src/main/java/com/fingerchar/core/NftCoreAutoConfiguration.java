package com.fingerchar.core;

import com.fingerchar.core.config.properties.FingerProperties;
import com.fingerchar.core.config.properties.StorageProperties;
import com.fingerchar.core.storage.IpfsStorage;
import com.fingerchar.core.storage.LocalStorage;
import com.fingerchar.core.storage.PinataStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 定义nft core config
 *
 * @author lance
 * @since 2024/10/17 18:04
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({FingerProperties.class, StorageProperties.class})
public class NftCoreAutoConfiguration {
  private final StorageProperties storageProperties;
  private final RestTemplate restTemplate;

  @Bean(name = "localStorage")
  public LocalStorage localStorage() {
    return new LocalStorage(storageProperties);
  }

  @Bean(name = "baseStorage")
  @ConditionalOnProperty(name = "com.finger.storage-type", havingValue = "ipfs-default")
  public IpfsStorage ipfsStorage() {
    return new IpfsStorage(storageProperties);
  }

  @Bean(name = "baseStorage")
  @ConditionalOnProperty(name = "com.finger.storage-type", havingValue = "pinata")
  public PinataStorage pinataStorage() {
    return new PinataStorage(storageProperties, restTemplate);
  }
}
