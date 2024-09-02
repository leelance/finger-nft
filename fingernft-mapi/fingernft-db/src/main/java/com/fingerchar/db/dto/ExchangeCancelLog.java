package com.fingerchar.db.dto;

import lombok.Data;

import java.math.BigInteger;

/**
 * ExchangeCancelLog
 *
 * @author Zjm
 * @since 2024/8/18 0:26
 */
@Data
public class ExchangeCancelLog {
  private String sellToken;

  private BigInteger sellTokenId;

  private String owner;

  private String buyToken;

  private BigInteger buyTokenId;

  private BigInteger salt;

  private String txHash;

  private BigInteger blockTimestamp;

  public ExchangeCancelLog(
      String sellToken,
      BigInteger sellTokenId,
      String owner,
      String buyToken,
      BigInteger buyTokenId,
      BigInteger salt,
      String txHash,
      BigInteger blockTimestamp
  ) {
    this.sellToken = sellToken;
    this.sellTokenId = sellTokenId;
    this.owner = owner;
    this.buyToken = buyToken;
    this.buyTokenId = buyTokenId;
    this.salt = salt;
    this.txHash = txHash;
    this.blockTimestamp = blockTimestamp;
  }

}
