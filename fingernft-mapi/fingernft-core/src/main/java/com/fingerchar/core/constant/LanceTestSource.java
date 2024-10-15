package com.fingerchar.core.constant;

import com.fingerchar.db.domain.FcStorage;
import lombok.experimental.UtilityClass;

/**
 * 测试数据
 *
 * @author lance
 * @since 2024/9/17 13:13
 */
@UtilityClass
public class LanceTestSource {

  public static FcStorage upload(){
    FcStorage s = new FcStorage();
    s.setId(1000L);
    s.setUrl("https://raw.seadn.io/files/c898fd8aa8820e5b6a7af258626731d7.svg");
    s.setIpfshash("ipfs");
    s.setKey("1000");
    return s;
  }
}
