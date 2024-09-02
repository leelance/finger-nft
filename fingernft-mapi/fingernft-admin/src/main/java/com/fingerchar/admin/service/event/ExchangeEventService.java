package com.fingerchar.admin.service.event;

import com.fingerchar.core.manager.FcContractNftManager;
import com.fingerchar.core.manager.FcSystemConfigManager;
import com.fingerchar.core.util.DappEventUtils;
import com.fingerchar.core.util.DappWeb3jUtil;
import com.fingerchar.db.dto.ConfigContract;
import com.fingerchar.db.dto.ExchangeBuyLog;
import com.fingerchar.db.dto.ExchangeCancelLog;
import com.fingerchar.db.vo.EventValuesExt;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthLog;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * exchange event
 *
 * @author admin
 * @since 2024/8/17 21:46
 */
@Service
@RequiredArgsConstructor
public class ExchangeEventService extends com.fingerchar.admin.service.event.baseEventService {
  private final FcSystemConfigManager systemConfigManager;
  private final FcContractNftManager contractNftManager;

  @Transactional(rollbackFor = Exception.class)
  public List<EventValuesExt> processEvent(List<EthLog.LogResult> allLogs, Map<BigInteger, EthBlock.Block> blockMap) throws Exception {
    //获取以下事件相关的合约列表
    ConfigContract configContract = systemConfigManager.getConfigContract();
    String exchangeAddress = configContract.getNftExchangeAddress();
    if (StringUtils.isEmpty(exchangeAddress)) {
      throw new Exception("Unset nft exchange address");
    }
    if (!DappWeb3jUtil.isValidAddress(exchangeAddress)) {
      throw new Exception("exchange address is unvalid");
    }
    List<EventValuesExt> list = processBuyEvent(exchangeAddress, allLogs, blockMap);
    //cancel事件 交易 取消事件
    List<EventValuesExt> list1 = processCancelEvent(exchangeAddress, allLogs, blockMap);
    list.addAll(list1);
    return list;
  }

  /**
   * 处理buy事件
   *
   * @param address 市场地址列表
   * @param allLogs 日志列表
   * @throws Exception
   */
  @Transactional(rollbackFor = Exception.class)
  public List<EventValuesExt> processBuyEvent(String address, List<EthLog.LogResult> allLogs, Map<BigInteger, EthBlock.Block> blockMap) throws Exception {
    //根据事件的合约地址与topic过滤logs
    List<EventValuesExt> buyList = getEventList(address, DappEventUtils.BUY_TOPIC, DappEventUtils.BUY_EVENT, allLogs, blockMap);
    if (buyList.isEmpty()) {
      return buyList;
    }

    for (int i = 0; i < buyList.size(); i++) {
      this.processBuyEvent(buyList.get(i));
    }
    return buyList;
  }

  /**
   * 处理每一条购买事件
   *
   * @param eventValues 单条事件记录
   * @throws Exception
   */
  private void processBuyEvent(EventValuesExt eventValues) throws Exception {
    String sellToken = (String) eventValues.getIndexedValues().get(0).getValue();
    BigInteger sellTokenId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
    BigInteger sellValue = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
    String owner = (String) eventValues.getNonIndexedValues().get(1).getValue();
    String buyToken = (String) eventValues.getNonIndexedValues().get(2).getValue();
    BigInteger buyTokenId = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
    BigInteger buyValue = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
    String buyer = (String) eventValues.getNonIndexedValues().get(5).getValue();
    BigInteger amount = (BigInteger) eventValues.getNonIndexedValues().get(6).getValue();
    BigInteger salt = (BigInteger) eventValues.getNonIndexedValues().get(7).getValue();

    ExchangeBuyLog log = new ExchangeBuyLog(sellToken, sellTokenId, sellValue, owner, buyToken, buyTokenId, buyValue, buyer, amount, salt, eventValues.getTxHash(), eventValues.getBlockTimestamp());

    this.contractNftManager.buy(log);
  }


  /**
   * processCancelEvent
   *
   * @param address 交易合约地址
   * @param allLogs 日志列表
   * @throws Exception ex
   */
  @Transactional(rollbackFor = Exception.class)
  public List<EventValuesExt> processCancelEvent(String address, List<EthLog.LogResult> allLogs, Map<BigInteger, EthBlock.Block> blockMap) throws Exception {
    //根据事件的合约地址与topic过滤logs
    List<EventValuesExt> cancelList = this.getEventList(address, DappEventUtils.CANCEL_TOPIC, DappEventUtils.CANCEL_EVENT, allLogs, blockMap);

    if (cancelList.isEmpty()) {
      return cancelList;
    }
    for (EventValuesExt eventValuesExt : cancelList) {
      processCancelEvent(eventValuesExt);
    }
    return cancelList;
  }

  /**
   * @param eventValues 日志
   * @throws Exception
   */
  private void processCancelEvent(EventValuesExt eventValues) throws Exception {
    String sellToken = (String) eventValues.getIndexedValues().get(0).getValue();
    BigInteger sellTokenId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
    String owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
    String buyToken = (String) eventValues.getNonIndexedValues().get(1).getValue();
    BigInteger buyTokenId = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
    BigInteger salt = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();

    ExchangeCancelLog log = new ExchangeCancelLog(sellToken, sellTokenId, owner, buyToken, buyTokenId, salt, eventValues.getTxHash(), eventValues.getBlockTimestamp());

    this.contractNftManager.cancel(log);
  }


}
