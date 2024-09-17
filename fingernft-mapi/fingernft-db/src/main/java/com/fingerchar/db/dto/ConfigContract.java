package com.fingerchar.db.dto;

import lombok.Data;

/**
 * ConfigContract
 * <a href="https://fingernft-doc.fingerchar.com/zh/config/CONTRACT.html">CONTRACT...</a>
 *
 * @author Zjm
 * @since 2022/3/30 19:27
 */
@Data
public class ConfigContract {
  /**
   * 发送一次就能查询多个链上数据
   */
  private String multiCallAddress;
  /**
   * 系统默认的ERC721合约
   */
  private String nft721Address;
  /**
   * 系统默认的ERC1155合约
   */
  private String nft1155Address;
  /**
   * 授权ERC721、ERC1155转移,与transferProxy不同的是，
   * 它可以转移老版本的ERC721、ERC1155的函数，需要加入这个合约白名单，就能调用它的转移接口
   */
  private String transferProxyForDeprecatedAddress;
  /**
   * 授权ERC20代币转账，需要加入这个合约白名单，就能调用它的转账接口
   */
  private String erc20TransferProxyAddress;
  private String exchangeStateAddress;
  private String exchangeOrdersHolderAddress;
  private String transferProxyAddress;
  private String nftExchangeAddress;

}
