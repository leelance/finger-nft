package com.fingerchar.admin.service.event;


import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.manager.FcSystemConfigManager;
import com.fingerchar.core.manager.FcTxOrderManager;
import com.fingerchar.db.vo.EventValuesExt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthLog;

import java.math.BigInteger;
import java.util.*;

/**
 * event service
 *
 * @author admin
 * @since 2024/8/17 20:57
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EventService extends com.fingerchar.admin.service.event.baseEventService {
  private final ExchangeEventService exchangeEventService;
  private final NftEventService nftEventService;
  private final FcTxOrderManager txOrderManager;
  private final FcSystemConfigManager systemConfigManager;

  @SuppressWarnings("all")
  @Transactional(rollbackFor = Exception.class)
  public void process(List<EthLog.LogResult> allLogs, Map<BigInteger, EthBlock.Block> blockMap) throws Exception {
    List<EventValuesExt> list = processEvent(allLogs, blockMap);
    //处理区块最新时间
    processTime(blockMap);
    //save tx log
    saveTxLogList(list);
    //save block info
    saveBlockInfo(blockMap);
  }

  private void processTime(Map<BigInteger, EthBlock.Block> blockMap) {
    //获得blockMap中最大的key
    Set<BigInteger> bigIntegers = blockMap.keySet();
    Object[] objects = bigIntegers.toArray();
    Arrays.sort(objects);

    BigInteger maxBlock = new BigInteger(objects[objects.length - 1].toString());
    Long time = blockMap.get(maxBlock).getTimestamp().longValue();
    log.info("===>block timestamp: {}", time);
  }

  private void saveTxLogList(List<EventValuesExt> list) {
    Map<String, EventValuesExt> map = new HashMap<>();
    for (EventValuesExt valuesExt : list) {
      map.put(valuesExt.getTxHash(), valuesExt);
    }
    list = new ArrayList<>(map.values());
    this.txOrderManager.saveBatch(list);
  }

  private void saveBlockInfo(Map<BigInteger, EthBlock.Block> blockMap) {
    if (blockMap.isEmpty()) {
      return;
    }

    Iterator<BigInteger> it = blockMap.keySet().iterator();
    BigInteger last = BigInteger.ZERO;
    BigInteger tmp;
    while (it.hasNext()) {
      tmp = it.next();
      if (last.compareTo(tmp) < 0) {
        last = tmp;
      }
    }
    this.systemConfigManager.update(SysConfConstant.LAST_BLOCK, last.toString());
  }


  /**
   * 回调过程
   *
   * @param allLogs 事件日志
   * @throws Exception ex
   */
  @SuppressWarnings("all")
  private List<EventValuesExt> processEvent(List<EthLog.LogResult> allLogs, Map<BigInteger, EthBlock.Block> blockMap) throws Exception {
    // 处理交易日志
    List<EventValuesExt> list = exchangeEventService.processEvent(allLogs, blockMap);
    // 处理nft日志
    List<EventValuesExt> list1 = nftEventService.processEvent(allLogs, blockMap);
    list.addAll(list1);
    return list;
  }

}
