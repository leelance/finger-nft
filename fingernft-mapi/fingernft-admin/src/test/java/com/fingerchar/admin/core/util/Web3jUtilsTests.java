package com.fingerchar.admin.core.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

/**
 * web3j 测试
 * EIP-1559添加了基本费用、最高优先费用、最高费用三个字段，分别解释如下：
 * <p>
 * 基本费用（Base Fee）：在以太坊上发送交易所需的最低Gas价格。基本费随着上一区块的Gas使用量而动态调整。
 * 最高优先费用（MaxPriority Fee）：用户愿意向矿工支付的Gas价格，越高越可能更快被打包到区块中。即给矿工的小费，默认是2gwei。
 * 最高费用（Max Fee）：用户愿意支付的最高Gas价格（基本费用+优先费用）。
 * 使用原交易字段Gas Price的交易类型，现在被称为Type 1。
 * <p>
 * 使用新交易字段Base Fee、MaxPriority Fee、Max Fee，而不使用Gas Price的交易类型，现在被称为Type 2。
 *
 * @author lance
 * @since 2024/8/20 23:59
 */
@Slf4j
class Web3jUtilsTests {
  private static final String ADDRESS = "0xB3F8313Ba4bE0F6c5d1a37d3c6F0204A204eE7Be";
  private static long CHAIN_ID;
  private static Web3j web3j;

  @BeforeAll
  static void init() {
    web3j = Web3j.build(new HttpService("http://192.168.204.140:8545"));
    CHAIN_ID = web3j.ethChainId().getId();
  }

  @Test
  @Disabled("get the next available nonce")
  void getTransactionCount() throws IOException {
    EthGetTransactionCount response = web3j.ethGetTransactionCount(ADDRESS, DefaultBlockParameterName.LATEST).send();

    Response.Error error = response.getError();
    log.info("===>error: {}, get tx count: {}", error, response.getTransactionCount());
  }

  @Test
  @Disabled("get balance")
  void getBalance() throws IOException {
    //1000000000000000000000000000000000000000000000000000000000000000000
    String address = "0x5921d09B709B493da3685504990E7Ab84FA2Ab74";
    EthGetBalance response = web3j.ethGetBalance(address, DefaultBlockParameterName.EARLIEST).send();
    Response.Error error = response.getError();

    //150000000000000000
    log.info("===>error: {}, balance: {}", error, response.getBalance());
  }

  @Test
    //@Disabled("get last block")
  void getLastBlock() throws IOException {
    EthBlockNumber ethBlock = web3j.ethBlockNumber().send();
    Response.Error error = ethBlock.getError();
    log.info("===>error: {}, lastBlock: {}", error, ethBlock.getBlockNumber());
  }

  /**
   * 验证地址
   */
  @Test
  @Disabled("is valid address")
  void isValidAddress() {
    boolean result = WalletUtils.isValidAddress(ADDRESS);
    log.info("===>{}", result);
    Assertions.assertTrue(result);
  }

  /**
   * transaction method.
   */
  @Test
  @Disabled("eth send transaction")
  void ethSendTransaction() throws IOException {
    String from = "0x5921d09B709B493da3685504990E7Ab84FA2Ab74";
    EthGetTransactionCount countRes = web3j.ethGetTransactionCount(from, DefaultBlockParameterName.LATEST).send();
    BigInteger nonce = countRes.getTransactionCount().add(BigInteger.valueOf(1));

    EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
    BigInteger gasPrice = ethGasPrice.getGasPrice();
    BigInteger gasLimit = DefaultGasProvider.GAS_LIMIT;

    String to = "0x811bB0C5EB3664D7bB167Ebc1cD77322586B7f89";
    BigInteger value = Convert.toWei("1", Convert.Unit.ETHER).toBigInteger();
    String data = "";

    org.web3j.protocol.core.methods.request.Transaction transaction = new org.web3j.protocol.core.methods.request.Transaction(from, nonce, gasPrice, gasLimit, to, value, data);
    EthSendTransaction txRes = web3j.ethSendTransaction(transaction).send();

    String txHash = txRes.getTransactionHash();
    log.info("===>tx hash: {}", txHash);
  }

  @Test
  @Disabled("maxPriorityFeePerGas transaction not support")
  void ethSendRawTransaction() throws Exception {
    String to = "0x4c1ce6384b2e6003a42e49390dc76a2e1990beba";
    //0x1fcd01073fe6017920a97cc384bee72c98beb0002f7067c48ef957b21009685ab69ee768e38bd
    //0xd0dde6222bf868b1f85e377325ff49dcb6dc34fe
    String priKey = "0x2ab8e90bf36593d1d053cb8ad2c5e060e3082cb3713f5c820a6f1f97574a5c75";
    Credentials credentials = Credentials.create(priKey);
    //gasLimit * gasPrice + value
    //21000000000000 + 1000000000
    TransactionReceipt receipt = Transfer.sendFunds(web3j, credentials, to, BigDecimal.valueOf(1), Convert.Unit.WEI).send();
    String txHash = receipt.getTransactionHash();
    log.info("===>tx hash: {}", txHash);
  }

  /**
   * @throws IOException ex
   */
  @Test
  @Disabled("get transaction info")
  void getTransaction() throws IOException {
    String txHash = "0x0dee08c49177e3d5fb285992c44801ae0564f332e14c29ba59b93120d489321f";
    EthTransaction response = web3j.ethGetTransactionByHash(txHash).send();

    Optional<TransactionReceipt> opt = web3j.ethGetTransactionReceipt(txHash).send().getTransactionReceipt();
    opt.ifPresent(tr -> {
      log.info("===>get tx receipt: {}", tr);
    });

    Response.Error error = response.getError();
    log.info("===>error: {}, get tx: {}", error, response.getRawResponse());
  }

  //======== contract =========

  /**
   * txHash:
   * 0xf41ec1b9e584eeb14bba575df95dd5681bd16baf16e5b0fa2eb1ad0bec223d45
   * 0x22518fb1f4ce21a79078c93913420f20cc0de0f46da55acd9174e929cf2dc7b7
   * <p>
   * contractAddress:
   * 0xd5E62AfF1Fc601c604057704268c582807C44345
   * 0xaA1C5b90acD59C252Dd92988424ee32C6008f0Ad
   */
  @Test
  @Disabled("deploy contract")
  void deployContract() throws Exception {
    //0xeafa1949c17d9974767166114c327566a3b43b29
    String priKey = "0xcbc90a6db409f7df2ce90f0f91e1c120ff5628e393bd8b8b1665d92ec5561f3d";
    Credentials credentials = Credentials.create(priKey);

    EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
    BigInteger gasPriceRes = ethGasPrice.getGasPrice();

    BigInteger gasLimit = DefaultGasProvider.GAS_LIMIT;
    ContractGasProvider contractGasProvider = new StaticGasProvider(gasPriceRes, gasLimit);

    ContractStorage contract = ContractStorage.deploy(web3j, credentials, contractGasProvider).send();
    String contractAddress = contract.getContractAddress();

    log.info("===>contract address: {}", contractAddress);
    Assertions.assertNotNull(contractAddress);
  }

  @AfterAll
  static void close() {
    web3j.shutdown();
  }
}
