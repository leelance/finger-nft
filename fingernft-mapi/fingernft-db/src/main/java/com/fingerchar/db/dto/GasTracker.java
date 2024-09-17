package com.fingerchar.db.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * GasTracker
 *
 * @author Zjm
 * @since 2024/9/17 10:01
 */
@Data
@NoArgsConstructor
public class GasTracker {
  private String low;
  private String medium;
  private String high;
  private String lastBlock;

  public GasTracker(BigInteger low, BigInteger medium, BigInteger high, BigInteger lastBlock) {
    this.low = low.toString();
    this.medium = medium.toString();
    this.high = high.toString();
    this.lastBlock = lastBlock.toString();
  }
}
