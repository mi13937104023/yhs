package com.ddzn.dd.framework.common.config;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class FastJsonLongToStringConfig {

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        // 创建 FastJSON 消息转换器
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        // FastJSON 配置
        FastJsonConfig fastJsonConfig = new FastJsonConfig();

        // 全局 Long → String
        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
        serializeConfig.put(Long.class, ToStringSerializer.instance);
        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
        fastJsonConfig.setSerializeConfig(serializeConfig);

        // 序列化特性
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.WriteMapNullValue, // 输出空字段
                SerializerFeature.WriteNullListAsEmpty, // List为空输出 []
                SerializerFeature.WriteNullStringAsEmpty, // String 为空输出 ""
                SerializerFeature.DisableCircularReferenceDetect // 禁用循环引用检测
        );

        fastJsonConfig.setCharset(StandardCharsets.UTF_8);

        fastConverter.setFastJsonConfig(fastJsonConfig);

        // 处理中文乱码
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        fastConverter.setSupportedMediaTypes(mediaTypes);

        return new HttpMessageConverters(fastConverter);
    }
}
