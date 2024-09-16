package com.fingerchar.api.service;

import com.fingerchar.api.utils.JwtHelper;

/**
 * 维护用户token
 *
 * @author admin
 */
public class UserTokenManager {

  public static String generateToken(String address, String tokenSecret) {
    return JwtHelper.createToken(address, tokenSecret);
  }
}
