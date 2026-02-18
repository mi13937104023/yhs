package com.ddzn.dd.gateway.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class GwCorsFilter {

    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); // 允许cookies跨域
        // 使用 setAllowedOriginPatterns 支持通配符，兼容 allowCredentials
        List<String> allowedOrigins = new ArrayList<>();
        allowedOrigins.add("http://www.dianiot.com");      // 正式域名
        allowedOrigins.add("http://api.dianiot.com");
        allowedOrigins.add("http://manager.dianiot.com");
        allowedOrigins.add("https://www.dianiot.com");      // 正式域名
        allowedOrigins.add("https://api.dianiot.com");
        allowedOrigins.add("https://manager.dianiot.com");
        allowedOrigins.add("http://192.168.*.*");
        allowedOrigins.add("http://172.20.*.*");
        config.setAllowedOriginPatterns(allowedOrigins);
        //config.addAllowedOrigin("*");// #允许向该服务器提交请求的URI，*表示全部允许，在SpringMVC中，如果设成*，会自动转成当前请求头中的Origin
        config.addAllowedHeader("*");// #允许访问的头信息,*表示全部
        config.addAllowedMethod("*");// 允许提交请求的方法类型，*表示全部允许
        config.setMaxAge(18000L);// 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了

        org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource source =
                new org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}