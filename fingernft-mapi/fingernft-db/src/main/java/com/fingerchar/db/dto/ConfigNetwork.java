package com.fingerchar.db.dto;

import lombok.Data;

/**
 * ConfigNetwork
 *
 * @author Zjm
 * @since 2024/8/18 0:26
 */
@Data
public class ConfigNetwork {
  private String network;
  private Integer chainId;
  private String name;
  private String symbol;
  private String explorer;
  private String opensea;
  private String rpc;
  private Integer blockTime;


}
