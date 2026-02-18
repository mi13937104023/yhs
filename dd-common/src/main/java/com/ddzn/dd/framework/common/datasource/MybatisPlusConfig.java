package com.ddzn.dd.framework.common.datasource;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.ddzn.dd.framework.common.util.security.SecurityUtils;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * @ClassName : MybatisPlusConfig  //类名
 * @Description :   //描述
 * @Author :   //作者
 */
@EnableTransactionManagement
@Configuration
@RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
public class MybatisPlusConfig {

    @Value("${dd.tenant.ignore-table}")
    private String ignoreTable = "";

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                if (ObjectUtil.isNotNull(SecurityUtils.getRequest())) {
                    String tenantId = SecurityUtils.getRequest().getHeader("tenant_id");
                    if (StringUtils.isNotBlank(tenantId)) {
                        try {
                            return new LongValue(tenantId);
                        } catch (NumberFormatException e) {
                            return new LongValue(1);
                        }
                    }
                    return new LongValue(1);
                } else {
                    return new LongValue(1);
                }
            }

            @Override
            public String getTenantIdColumn() {
                // 你的租户字段名
                return "tenant_id";
            }

            @Override
            public boolean ignoreTable(String tableName) {
                if (ignoreTable != null) {
                    if (tableName.startsWith("t_gps")) {
                        return true;
                    } else {
                        return ignoreTable.contains(tableName + ",");
                    }
                }
                return false;
            }
        }));

        // 注意多租户插件要先注册
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());


        return interceptor;
    }
}



