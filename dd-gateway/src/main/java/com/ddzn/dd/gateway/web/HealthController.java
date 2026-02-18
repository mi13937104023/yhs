package com.ddzn.dd.gateway.web;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查
 */
@Api("健康检查")
@RestController
public class HealthController {
    /**
     * 健康检查
     *
     * @return
     */
    @GetMapping(path = "health")
    public String health() {
        return "success";
    }
}
