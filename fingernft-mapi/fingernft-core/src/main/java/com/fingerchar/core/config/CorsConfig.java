package com.fingerchar.core.config;

import com.fingerchar.core.config.properties.FingerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * cors config
 *
 * @author admin
 * @since 2024/9/16 16:28
 */
@Configuration
@RequiredArgsConstructor
public class CorsConfig {
  private final FingerProperties fingerProperties;

  private CorsConfiguration buildConfig() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    // 1 设置访问源地址
    corsConfiguration.addAllowedOrigin("*");
    // 2 设置访问源请求头
    corsConfiguration.addAllowedHeader("*");
    // 3 设置访问源请求方法
    corsConfiguration.addAllowedMethod("*");
    corsConfiguration.setMaxAge(fingerProperties.getCorsMaxAge());
    return corsConfiguration;
  }

  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    // 4 对接口配置跨域设置
    source.registerCorsConfiguration("/**", buildConfig());
    return new CorsFilter(source);
  }
}
