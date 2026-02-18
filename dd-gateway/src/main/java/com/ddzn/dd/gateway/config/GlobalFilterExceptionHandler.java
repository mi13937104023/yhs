package com.ddzn.dd.gateway.config;

import cn.hutool.core.util.ObjectUtil;
import com.ddzn.dd.model.base.BusinessException;
import com.ddzn.dd.model.base.ResponseResult;
import com.ddzn.dd.model.constant.HttpStatusCodeConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * filter 中的异常处理
 *
 * @author DaiJuZheng
 * @date 2022/5/27 10:33
 **/
@Slf4j
@Order(-1)
@Configuration
public class GlobalFilterExceptionHandler implements ErrorWebExceptionHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest request = exchange.getRequest();
        if (response.isCommitted()) {
            return Mono.error(throwable);
        }
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                ResponseResult result;
                //返回响应结果
                if (throwable instanceof BusinessException) {
                    BusinessException businessException = (BusinessException) throwable;
                    if (ObjectUtil.isNotNull(businessException.getCode()) && businessException.getCode().equals(HttpStatusCodeConstants.NO_LOGIN.toString())) {
                        // token 非法
                        result = ResponseResult.failed(HttpStatusCodeConstants.NO_LOGIN, throwable.getMessage());
                    } else {
                        result = ResponseResult.failed(businessException.getMessage());
                    }
                } else if (throwable instanceof ResponseStatusException) {
                    ResponseStatusException responseStatusException = (ResponseStatusException) throwable;
                    result = ResponseResult.failed(responseStatusException.getMessage());
                } else {
                    // 其它异常类型处理
                    log.error("其它异常类型处理:{}", throwable.getMessage(), throwable);
                    result = ResponseResult.failed("未知异常");
                }
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(result));
            } catch (JsonProcessingException e) {
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }
}
