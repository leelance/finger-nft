package com.fingerchar.api.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.api.utils.JwtHelper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.config.properties.FingerProperties;
import com.fingerchar.db.domain.FcUserToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于非必须Token接口，实现用户ID配置到RequestAttribute
 *
 * @author admin
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OptionalTokenInterceptor implements HandlerInterceptor {
  private final FingerProperties fingerProperties;
  private final IBaseService baseService;

  @Override
  public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
    if (log.isDebugEnabled()) {
      log.debug("===>web request: {}", request.getRequestURL());
    }

    String token = request.getHeader("Finger-Nft-Token");

    if (!StringUtils.isEmpty(token) && !StringUtils.isEmpty((JwtHelper.verifyTokenAndGetUserAddress(token, fingerProperties.getTokenSecret())))) {
      QueryWrapper<FcUserToken> wrapper = new QueryWrapper<>();
      String userAddress = JwtHelper.verifyTokenAndGetUserAddress(token, fingerProperties.getTokenSecret());
      wrapper.eq(FcUserToken.USER_ADDRESS, userAddress);
      FcUserToken userToken = this.baseService.getByCondition(FcUserToken.class, wrapper);
      if (null != userToken && null != userToken.getUserToken() && userToken.getUserToken().equals(token)) {
        request.setAttribute("userAddress", userAddress);
      }
    }
    return true;
  }
}
