package com.ddzn.dd.gateway.service;

import com.ddzn.dd.framework.common.util.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author DaiJuZheng
 * @date 2022/5/25 18:21
 **/
@Service
public class GetService {
    @Resource
    private TokenUtils tokenUtils;

    /**
     * 暂时处理， GlobalFilter 中同步调用 FeignClient 会报错
     *
     * @param token 获取的token
     * @return String
     * @author DaiJuzheng
     * @date 2022/5/25 19:02
     **/
    public Long verifyUserIdByToken(String token, String source) throws ExecutionException, InterruptedException {
        return tokenUtils.parseUserId(token, source);
    }
}
