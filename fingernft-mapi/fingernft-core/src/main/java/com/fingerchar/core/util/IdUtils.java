package com.fingerchar.core.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

/**
 * Id generate
 *
 * @author lance
 * @since 2024/10/19 17:39
 */

@Slf4j
@UtilityClass
public final class IdUtils {
  private static final Snowflake SNOW_FLAKE = Snowflake.createDefault();

  /**
   * 产生Snowflake.id
   *
   * @return id
   */
  public static long nextId() {
    return SNOW_FLAKE.nextId();
  }

  /**
   * 产生id, 通过snowflakes, 增加一个前缀
   *
   * @param prefix 前缀
   * @return id
   */
  public static String nextId(String prefix) {
    return prefix + SNOW_FLAKE.nextId();
  }

  /**
   * 产生id, 根据字符串, 算md5 hash
   *
   * @param prefix     prefix
   * @param topic      topic
   * @param consumerId consumerId
   * @param partition  partition
   * @return md5 hash
   */
  public static String nextId(String prefix, String topic, String consumerId, int partition) {
    String trace = String.format("%s:%s:%s:%d", prefix, topic, consumerId, partition);
    if (log.isDebugEnabled()) {
      log.debug("===>calc traceId: {}", trace);
    }
    return DigestUtils.md5DigestAsHex(trace.getBytes(StandardCharsets.UTF_8));
  }

  private static class Snowflake {
    /**
     * Sign bit, Unused (always set to 0)
     */
    private static final int EPOCH_BITS = 41;
    private static final int NODE_ID_BITS = 10;
    private static final int SEQUENCE_BITS = 12;

    private static final long MAX_NODE_ID = (1L << NODE_ID_BITS) - 1;
    private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1;

    /**
     * Custom Epoch (2023-08-08 08:08:08)
     */
    private static final long DEFAULT_CUSTOM_EPOCH = 1691453288000L;
    /**
     * Node ID - 10 bits. This gives us 1024 nodes/machines.
     */
    private final long nodeId;
    /**
     * Epoch timestamp in milliseconds precision - 41 bits.
     * The maximum timestamp that can be represented using 41 bits is 2^41 - 1, or 2199023255551,
     * which comes out to be Wednesday, September 7, 2039 3:47:35.551 PM.
     * That gives us 69 years with respect to a custom epoch
     */
    private final long customEpoch;

    private volatile long lastTimestamp = -1L;
    private volatile long sequence = 0L;

    /**
     * Create Snowflake with a nodeId and custom epoch
     *
     * @param nodeId      nodeId
     * @param customEpoch customEpoch
     */
    public Snowflake(long nodeId, long customEpoch) {
      if (nodeId < 0 || nodeId > MAX_NODE_ID) {
        throw new IllegalArgumentException(String.format("NodeId must be between %d and %d", 0, MAX_NODE_ID));
      }
      this.nodeId = nodeId;
      this.customEpoch = customEpoch;

      log.warn("===>Snowflake generatorId: {}", this.nodeId);
    }

    /**
     * Create Snowflake with a nodeId
     *
     * @param nodeId nodeId
     */
    public Snowflake(long nodeId) {
      this(nodeId, DEFAULT_CUSTOM_EPOCH);
    }

    public static Snowflake createDefault() {
      int seed = RandomUtils.nextInt(0, 1024);
      return new Snowflake(seed);
    }

    /**
     * Next id
     *
     * @return long
     */
    public synchronized long nextId() {
      long currentTimestamp = timestamp();

      if (currentTimestamp < lastTimestamp) {
        throw new IllegalStateException("Invalid System Clock!");
      }

      if (currentTimestamp == lastTimestamp) {
        sequence = (sequence + 1) & MAX_SEQUENCE;
        if (sequence == 0) {
          // Sequence Exhausted, wait till next millisecond.
          currentTimestamp = waitNextMillis(currentTimestamp);
        }
      } else {
        // reset sequence to start with zero for the next millisecond
        sequence = 0;
      }

      lastTimestamp = currentTimestamp;
      return currentTimestamp << (NODE_ID_BITS + SEQUENCE_BITS) | (nodeId << SEQUENCE_BITS) | sequence;
    }


    /**
     * Get current timestamp in milliseconds, adjust for the custom epoch.
     *
     * @return timestamp
     */
    private long timestamp() {
      return Instant.now().toEpochMilli() - customEpoch;
    }

    /**
     * Block and wait till next millisecond
     *
     * @param currentTimestamp current timestamp
     * @return long
     */
    private long waitNextMillis(long currentTimestamp) {
      while (currentTimestamp == lastTimestamp) {
        currentTimestamp = timestamp();
      }
      return currentTimestamp;
    }

    @Override
    public String toString() {
      return "Snowflake Settings [EPOCH_BITS=" + EPOCH_BITS + ", NODE_ID_BITS=" + NODE_ID_BITS
          + ", SEQUENCE_BITS=" + SEQUENCE_BITS + ", CUSTOM_EPOCH=" + customEpoch
          + ", NodeId=" + nodeId + "]";
    }
  }
}

