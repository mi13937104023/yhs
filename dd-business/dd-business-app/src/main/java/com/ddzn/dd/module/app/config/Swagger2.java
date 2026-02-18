package com.ddzn.dd.module.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @name: Swagger2
 * @description: XXX
 * @type: JAVA
 * @since:
 * @author:
 */
@Configuration
@EnableSwagger2
@Profile({"dev", "local","test"})
public class Swagger2 {

    @Bean
    public Docket authApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("业务接口")
                .apiInfo(authApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ddzn.dd.module.app.controller"))
                .paths(PathSelectors.any())
                .build().globalOperationParameters(setParam()).pathMapping("/system-business");
    }

    /**
     * 设置参数
     *
     * @return
     */
    private List<Parameter> setParam() {
        List<Parameter> parameters = new ArrayList<Parameter>();

        ParameterBuilder appkey = new ParameterBuilder();
        appkey.name("appkey").description("租户ID").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        appkey.defaultValue("test");
        parameters.add(appkey.build());

        ParameterBuilder nonceStr = new ParameterBuilder();
        nonceStr.name("noncestr").description("随机数").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        nonceStr.defaultValue("1653619501");
        parameters.add(nonceStr.build());

        ParameterBuilder version = new ParameterBuilder();
        version.name("version").description("版本号").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        version.defaultValue("1.0.0");
        parameters.add(version.build());

        ParameterBuilder sign = new ParameterBuilder();
        sign.name("sign").description("签名").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        sign.defaultValue("test-my-self");
        parameters.add(sign.build());

        ParameterBuilder timeStamp = new ParameterBuilder();
        timeStamp.name("timestamp").description("时间戳").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        timeStamp.defaultValue(String.valueOf(System.currentTimeMillis()));
        parameters.add(timeStamp.build());

        ParameterBuilder authParam = new ParameterBuilder();
        authParam.name("Authorization").description("Bearer token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        parameters.add(authParam.build());

        return parameters;
    }

    private ApiInfo authApiInfo() {
        return new ApiInfoBuilder().title("业务接口")
                .version("1.0.0")
                .description("业务接口")
                .contact(new Contact("", "", ""))
                .termsOfServiceUrl("")
                .build();
    }

}

