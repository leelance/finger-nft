package com.fingerchar.admin.task;

import com.fingerchar.admin.service.GasTrackerService;
import com.fingerchar.admin.service.event.EventService;
import com.fingerchar.admin.vo.AllLogsVo;
import com.fingerchar.core.common.consts.SysConfConst;
import com.fingerchar.core.common.exception.ServiceException;
import com.fingerchar.core.manager.FcSystemConfigManager;
import com.fingerchar.core.manager.FcTxOrderManager;
import com.fingerchar.core.common.result.ResultCode;
import com.fingerchar.core.util.DappWeb3jUtil;
import com.fingerchar.core.util.StringConst;
import com.fingerchar.db.domain.FcTxOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Log;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * schedule task
 *
 * @author admin
 * @since 2024/8/17 19:56
 */
@Slf4j
@EnableAsync
@Configuration
@EnableScheduling
@RequiredArgsConstructor
@DependsOn({"initRunner"})
public class ScheduleTask {
  private final FcSystemConfigManager systemConfigManager;
  private final GasTrackerService gasTrackerService;
  private final FcTxOrderManager txOrderManager;
  private final EventService eventService;
  private static final AtomicBoolean SYNC_BLOCK = new AtomicBoolean(false);

  /**
   * start sync block
   */
  @Scheduled(cron = "0 */30 * * * ?")
  public void syncEthLastBlock() {
    if (SYNC_BLOCK.compareAndSet(true, false)) {
      log.info("task is in processing status");
      return;
    }

    try {
      String startStr = this.systemConfigManager.getKeyValue(SysConfConst.LAST_BLOCK);
      if (null == startStr) {
        //have no config last_block
        throw ServiceException.of(ResultCode.SYS_CFG_LAST_BLK_ERR);
      }
      //start
      BigInteger start = new BigInteger(startStr);
      String blockConfirmation = this.systemConfigManager.getKeyValue(SysConfConst.BLOCK_CONFIRMATION);
      start = start.add(BigInteger.ONE);
      //web3j 获取最新区块
      BigInteger end = DappWeb3jUtil.getLastBlock().subtract(new BigInteger(blockConfirmation));
      log.info("===>block start: {}, end: {}", start, end);
      if (start.compareTo(end) > 0) {
        return;
      }

      long maxBlockOneTime = Long.parseLong(this.systemConfigManager.getKeyValue(SysConfConst.MAX_BLOCK_ONE_TIME));
      if (end.subtract(start).longValue() > maxBlockOneTime) {
        end = start.add(BigInteger.valueOf(maxBlockOneTime));
      }

      AllLogsVo allLogsVo = getEthLogs(start, end);
      @SuppressWarnings("all")
      List<EthLog.LogResult> allLogs = allLogsVo.getAllLogs();
      end = allLogsVo.getEnd();
      allLogs = filterExistedLog(allLogs, start, end);
      log.info("===>get eth block start: {}, end: {}", start, end);
      Map<BigInteger, EthBlock.Block> blockMap = getBlockInfo(start, end);

      eventService.process(allLogs, blockMap);
    } catch (Exception e) {
      log.error("===>schedule task sync block fail: ", e);
    } finally {
      SYNC_BLOCK.set(true);
    }
  }

  @SuppressWarnings("all")
  @Scheduled(cron = "* 0/20 * * * ?")
  private void startGasTracker() {
    try {
      BigInteger lastBlock = DappWeb3jUtil.getLastBlock();
      BigInteger start = lastBlock.subtract(BigInteger.valueOf(3L));
      if (start.compareTo(BigInteger.ONE) < 0) {
        start = BigInteger.ONE;
      }

      //获取区块中tx信息
      List<EthBlock.Block> blockList = DappWeb3jUtil.getBlockList(start, lastBlock, true);
      List<BigInteger> gasPrices = new ArrayList<>();
      for (EthBlock.Block block : blockList) {
        List<EthBlock.TransactionResult> transactionHashList = block.getTransactions();
        for (EthBlock.TransactionResult transactionResult : transactionHashList) {
          gasPrices.add(((EthBlock.TransactionObject) transactionResult).getGasPrice());
        }
      }
      Collections.sort(gasPrices);

      this.gasTrackerService.getGasTracker(gasPrices, lastBlock);
    } catch (Exception e) {
      log.error("gasTracker task error", e);
    }
  }

  /**
   * get eth logs
   *
   * @param start block start
   * @param end   block end
   * @return all logs vo
   * @throws Exception ex
   */
  @SuppressWarnings("all")
  private AllLogsVo getEthLogs(BigInteger start, BigInteger end) throws Exception {
    EthLog ethLog = DappWeb3jUtil.getEthLogs(start, end);
    if (ethLog.hasError()) {
      log.error("===>web3j get eth logs code: {}, msg: {}", ethLog.getError().getCode(), ethLog.getError().getMessage());
      //如果是获取的log数超过10000条的错误，则减少单次获取的区块数量
      if (ethLog.getError().getCode() == -32005) {
        BigInteger size = end.subtract(start);
        if (start.equals(end)) {
          //获取单个区块logs时发生错误，请联系管理员
          throw ServiceException.of(ResultCode.WEB3J_GET_ETH_LOG_ERR);
        }

        size = size.divide(BigInteger.valueOf(2L));
        BigInteger newEnd = start.add(size);
        return getEthLogs(start, newEnd);
      } else {
        throw ServiceException.of(ethLog.getError().getCode() + StringConst.EMPTY, ethLog.getError().getMessage());
      }
    }

    List<EthLog.LogResult> allLogs = ethLog.getLogs();
    AllLogsVo allLogsVo = new AllLogsVo(allLogs, end);
    return allLogsVo;
  }

  private Map<BigInteger, EthBlock.Block> getBlockInfo(BigInteger start, BigInteger end) throws Exception {
    List<EthBlock.Block> blockList = DappWeb3jUtil.getBlockList(start, end);
    Map<BigInteger, EthBlock.Block> map = new HashMap<>();
    for (EthBlock.Block block : blockList) {
      map.put(block.getNumber(), block);
    }
    return map;
  }

  @SuppressWarnings("all")
  private List<EthLog.LogResult> filterExistedLog(List<EthLog.LogResult> allLogs, BigInteger start, BigInteger end) {
    List<FcTxOrder> txOrderList = txOrderManager.getList(Integer.valueOf(start.toString()), Integer.valueOf(end.toString()));
    Set<String> hashSet = txOrderList.stream().map(FcTxOrder::getTxHash).collect(Collectors.toSet());
    return allLogs
        .stream()
        .filter(log -> !hashSet.contains(((Log) (log.get())).getTransactionHash()))
        .collect(Collectors.toList());
  }

}
