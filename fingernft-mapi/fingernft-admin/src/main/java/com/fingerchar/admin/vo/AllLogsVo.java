package com.fingerchar.admin.vo;

import lombok.Getter;
import org.web3j.protocol.core.methods.response.EthLog;

import java.math.BigInteger;
import java.util.List;

/**
 * all logs vo
 *
 * @author admin
 * @since 2024/8/17 20:49
 */
@Getter
@SuppressWarnings("all")
public class AllLogsVo {
  private List<EthLog.LogResult> allLogs;
  private BigInteger end;

  public AllLogsVo(List<EthLog.LogResult> allLogs, BigInteger end) {
    this.allLogs = allLogs;
    this.end = end;
  }

  public void setAllLogs(List<EthLog.LogResult> allLogs) {
    this.allLogs = allLogs;
  }

  public void setEnd(BigInteger end) {
    this.end = end;
  }

  @Override
  public String toString() {
    return "AllLogsVo{" +
        "allLogs=" + allLogs +
        ", end=" + end +
        '}';
  }
}
