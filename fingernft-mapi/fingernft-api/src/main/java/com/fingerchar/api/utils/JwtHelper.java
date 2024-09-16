package com.fingerchar.api.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * jwt
 *
 * @author admin
 * @since 2024/9/16 16:20
 */
@Slf4j
@UtilityClass
public class JwtHelper {
  static final String ISSUER = "Finger-nft";
  static final String SUBJECT = "this is finger nft token";
  static final String AUDIENCE = "D-APP-CHAIN";

  public static String createToken(String address, String tokenSecret) {
    try {
      Map<String, Object> map = new HashMap<>();
      Date nowDate = new Date();
      // 过期时间：2小时, 上线时，需要修改过期时间
      Date expireDate = getAfterDate(nowDate, 0, 1, 0, 0, 0, 0);
      map.put("alg", "HS256");
      map.put("typ", "JWT");
      return JWT.create()
          .withHeader(map)
          .withClaim("address", address)
          .withIssuer(ISSUER)
          .withSubject(SUBJECT)
          .withAudience(AUDIENCE)
          // 生成签名的时间
          .withIssuedAt(nowDate)
          // 签名过期的时间
          .withExpiresAt(expireDate)
          // 签名 Signature
          .sign(Algorithm.HMAC256(tokenSecret));
    } catch (JWTCreationException ex) {
      log.warn("===>create jwt token fail: ", ex);
    }
    return null;
  }

  public static String verifyTokenAndGetUserAddress(String token, String tokenSecret) {
    try {
      JWTVerifier verifier = JWT.require(Algorithm.HMAC256(tokenSecret))
          .withIssuer(ISSUER)
          .build();
      DecodedJWT jwt = verifier.verify(token);
      Map<String, Claim> claims = jwt.getClaims();
      Claim claim = claims.get("address");
      if (null == claim) {
        return null;
      }
      return claim.asString();
    } catch (JWTVerificationException exception) {
      //ignore exception
    }

    return null;
  }

  public static Date getAfterDate(Date date, int year, int month, int day, int hour, int minute, int second) {
    if (date == null) {
      date = new Date();
    }

    Calendar cal = new GregorianCalendar();

    cal.setTime(date);
    if (year != 0) {
      cal.add(Calendar.YEAR, year);
    }
    if (month != 0) {
      cal.add(Calendar.MONTH, month);
    }
    if (day != 0) {
      cal.add(Calendar.DATE, day);
    }
    if (hour != 0) {
      cal.add(Calendar.HOUR_OF_DAY, hour);
    }
    if (minute != 0) {
      cal.add(Calendar.MINUTE, minute);
    }
    if (second != 0) {
      cal.add(Calendar.SECOND, second);
    }
    return cal.getTime();
  }

}
