package com.fingerchar.core.constant;

public class SysConfConstant {
  public static final String CONFIG_NETWORK = "configNetwork";
  public static final String CONFIG_DEPLOY = "configDeploy";
  public static final String CONFIG_CONTRACT = "configContract";
  /**
   * 在平台售卖成功的NFT，从卖方手里收取的手续费	250(收取2.5%手续费)
   */
  public static final String SELLER_FEE = "sellerFee";
  /**
   * 在平台售卖成功的NFT，从买方手里收取的手续费	250(收取2.5%手续费)
   */
  public static final String BUYER_FEE = "buyerFee";
  /**
   * 最新块
   */
  public static final String LAST_BLOCK = "lastBlock";

  public static final String NFT_DEFAULT_VERIFY = "nftDefaultVerify";

  public static final String LOGIN_MESSAGE = "loginMessage";

  public static final String WEBSITE = "website";

  public static final String IPFS_SERVER_IP = "ipfsServerIp";
  public static final String IPFS_SERVER_PORT = "ipfsServerPort";
  public static final String IPFS_REMOTE_SERVER = "ipfsRemoteServer";
  public static final String IPFS_URL = "ipfsUrl";
  public static final String GAS_TRACKER = "gasTracker";

  public static final String CDN_URL = "cdnUrl";

  public static final String STATIC_LOCAL_PATH = "staticLocalPath";

  public static final String ZERO_ADDRESS = "0x0000000000000000000000000000000000000000";
  /**
   * 设置需要延迟多少个块，值越大，数据越稳定；因为区块链是由很多节点组成的网络，节点间数据同步需要时间；设置该值要根据链的出块速度而定。
   * 12(如果出块速度在15秒以上)或30(如果出块速度在3秒左右)
   */
  public static final String BLOCK_CONFIRMATION = "blockConfirmation";
  /**
   * 每次从链上拉取多少块
   */
  public static final String MAX_BLOCK_ONE_TIME = "maxBlockOneTime";

  public static final String URL_PREFIX = "/fingernft";


}
