package com.fingerchar.core.storage.pinata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * <a href="https://api.pinata.cloud/pinning/pinFileToIPFS">Pin File to IPFS</a>
 * Upload a file to Pinata to be pinned to IPFS
 *
 * @author lance
 * @since 2024/10/19 10:53
 */
@Data
public class PinFileToIpfsDto {
  /**
   * ipfs hash
   */
  @JsonProperty("IpfsHash")
  private String ipfsHash;
  /**
   * file size
   */
  @JsonProperty("PinSize")
  private int pinSize;
  /**
   * 时间戳
   */
  @JsonProperty("Timestamp")
  private String timestamp;
  /**
   * 是否重复
   */
  private boolean isDuplicate;
}
