package com.fingerchar.api.vo;

import com.fingerchar.db.dto.ConfigContract;
import com.fingerchar.db.dto.ConfigNetwork;
import lombok.Data;

/**
 * @Author： Zjm
 * @Date：2022/3/31 9:04
 */
@Data
public class ConfigFetchVo {
  private String ipfsUrl;
  private String sellerFee;
  private String buyerFee;
  private String cdnUrl;

  private String miner;

  private String loginMessage;

  private String website;

  ConfigNetwork network;
  ConfigContract contract;
}
