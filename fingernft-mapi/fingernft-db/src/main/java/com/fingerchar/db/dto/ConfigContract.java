package com.fingerchar.db.dto;

import lombok.Data;

/**
 * ConfigContract
 *
 * @author Zjm
 * @since 2022/3/30 19:27
 */
@Data
public class ConfigContract {
  private String multiCallAddress;
  private String nft721Address;
  private String nft1155Address;
  private String transferProxyForDeprecatedAddress;
  private String erc20TransferProxyAddress;
  private String exchangeStateAddress;
  private String exchangeOrdersHolderAddress;
  private String transferProxyAddress;
  private String nftExchangeAddress;

}
