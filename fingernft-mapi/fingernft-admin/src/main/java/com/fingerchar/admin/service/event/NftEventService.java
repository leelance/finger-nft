package com.fingerchar.admin.service.event;

import com.fingerchar.admin.service.FcContractService;
import com.fingerchar.db.vo.EventValuesExt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthLog;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * nft event
 *
 * @author lance
 * @since 2024/8/17 21:55
 */
@Service
@RequiredArgsConstructor
public class NftEventService extends baseEventService {
  private final Erc721EventService erc721EventService;
  private final FcContractService contractService;

  public List<EventValuesExt> processEvent(List<EthLog.LogResult> allLogs, Map<BigInteger, EthBlock.Block> blockMap) throws Exception {
    //获取以下事件相关的合约列表
    List<String> addressList = getAllAddr();
    addressList = filterInvalidContract(addressList);
    //721 transfer事件
    return erc721EventService.processTransferEvent(addressList, allLogs, blockMap);
  }

  private List<String> getAllAddr() {
    return contractService.findAllAddress();
  }
}
