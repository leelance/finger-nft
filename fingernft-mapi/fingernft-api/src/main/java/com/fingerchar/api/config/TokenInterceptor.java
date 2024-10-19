package com.fingerchar.api.config;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.api.utils.JwtHelper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.config.properties.FingerProperties;
import com.fingerchar.core.common.consts.SysConst;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.core.util.json.JsonUtils;
import com.fingerchar.db.domain.FcUserToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Token拦截器
 *
 * @author admin
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenInterceptor implements HandlerInterceptor {
  private final FingerProperties fingerProperties;
  private final IBaseService baseService;

  @Override
  public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
    String token = request.getHeader(SysConst.WEB_TOKEN_NAME);
    if(log.isDebugEnabled()){
      log.debug("===>web token, {}, token：{}", request.getRequestURL(), token);
    }

    if (StringUtils.isEmpty(token) || StringUtils.isEmpty((JwtHelper.verifyTokenAndGetUserAddress(token, fingerProperties.getTokenSecret())))) {
      this.unLogin(response);
      return false;
    }

    String userAddress = JwtHelper.verifyTokenAndGetUserAddress(token, fingerProperties.getTokenSecret());
    QueryWrapper<FcUserToken> wrapper = new QueryWrapper<>();
    wrapper.eq(FcUserToken.USER_ADDRESS, userAddress);
    FcUserToken userToken = this.baseService.getByCondition(FcUserToken.class, wrapper);
    if (null == userToken || null == userToken.getUserToken() || !userToken.getUserToken().equals(token)) {
      this.unLogin(response);
      return false;
    }
    request.setAttribute("userAddress", userAddress);
    return true;
  }

  private void unLogin(HttpServletResponse response) throws IOException {
    Object obj = ResponseUtil.unlogin();
    response.setCharacterEncoding("utf-8");
    response.setContentType("application/json");
    response.getWriter().write(JsonUtils.toJsonString(obj));
  }
}
