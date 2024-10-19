package com.fingerchar.api.config;

import com.fingerchar.core.common.consts.SysConfConst;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web mvc
 *
 * @author admin
 * @since 2024/9/16 16:39
 */
@Configuration
@RequiredArgsConstructor
public class FcWebMvcConfiguration implements WebMvcConfigurer {
  private final TokenInterceptor tokenInterceptor;
  private final OptionalTokenInterceptor optionalTokenInterceptor;

  private static final String[] NOT_NEED_LOGIN_PATH = {
      SysConfConst.URL_PREFIX + "/user/login",
      SysConfConst.URL_PREFIX + "/user/reload",
      SysConfConst.URL_PREFIX + "/user/listbyaddr",
      SysConfConst.URL_PREFIX + "/user/follows",
      SysConfConst.URL_PREFIX + "/user/collections",
      SysConfConst.URL_PREFIX + "/user/nftlist",
      SysConfConst.URL_PREFIX + "/user/stat",
      SysConfConst.URL_PREFIX + "/user/info",
      SysConfConst.URL_PREFIX + "/user/onsales",
      SysConfConst.URL_PREFIX + "/user/like",
      SysConfConst.URL_PREFIX + "/user/created",
      SysConfConst.URL_PREFIX + "/user/white",
      SysConfConst.URL_PREFIX + "/user/listcontract",
      SysConfConst.URL_PREFIX + "/follow/match",
      SysConfConst.URL_PREFIX + "/follow/follows",
      SysConfConst.URL_PREFIX + "/follow/followers",
      SysConfConst.URL_PREFIX + "/category/list",
      SysConfConst.URL_PREFIX + "/contract/info",
      SysConfConst.URL_PREFIX + "/contract/getinfo",
      SysConfConst.URL_PREFIX + "/contract/stat",
      SysConfConst.URL_PREFIX + "/contract/list",
      SysConfConst.URL_PREFIX + "/contract/all",
      SysConfConst.URL_PREFIX + "/contract/listbyaddr",
      SysConfConst.URL_PREFIX + "/contract/onsales",
      SysConfConst.URL_PREFIX + "/contract/collections",
      SysConfConst.URL_PREFIX + "/contract/listitems",
      SysConfConst.URL_PREFIX + "/contract/listall",
      SysConfConst.URL_PREFIX + "/paytoken/list",
      SysConfConst.URL_PREFIX + "/home/list",
      SysConfConst.URL_PREFIX + "/home/indexlist",
      SysConfConst.URL_PREFIX + "/like/listuserlike",
      SysConfConst.URL_PREFIX + "/config/fetch",
      SysConfConst.URL_PREFIX + "/config/gasTracker",
      SysConfConst.URL_PREFIX + "/home/search",
      SysConfConst.URL_PREFIX + "/home/searchuser",
      SysConfConst.URL_PREFIX + "/nft/owners",
      SysConfConst.URL_PREFIX + "/nft/bids",
      SysConfConst.URL_PREFIX + "/nft/history",
      SysConfConst.URL_PREFIX + "/nft/detail",
      SysConfConst.URL_PREFIX + "/nft/activebids",
      SysConfConst.URL_PREFIX + "/nft/activesales",
      SysConfConst.URL_PREFIX + "/nft/getmedia",
      SysConfConst.URL_PREFIX + "/nft/getroyalties",
      SysConfConst.URL_PREFIX + "/order/get",
      SysConfConst.URL_PREFIX + "/notices/countunread",
      SysConfConst.URL_PREFIX + "/notices/list",
      SysConfConst.URL_PREFIX + "/notices/count",
      "/upload/*",
      "/static/*"
  };

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // 指定必选token的接口（需要登录）
    registry.addInterceptor(tokenInterceptor).excludePathPatterns(NOT_NEED_LOGIN_PATH);
    // 可选token接口（可不登录）
    registry.addInterceptor(optionalTokenInterceptor);
  }

  @Override
  public void addResourceHandlers(final ResourceHandlerRegistry register) {
    register.addResourceHandler("/static/**")
        .addResourceLocations("classpath:/static/upload/");
  }
}
