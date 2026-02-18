package com.ddzn.dd.module.system.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ddzn.dd.framework.common.util.TokenUtils;
import com.ddzn.dd.model.base.UserTokenResult;
import com.ddzn.dd.model.constant.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 基础controller
 *
 * @author Dax
 * @since 17 :34  2019-04-09
 */
@Slf4j
public class BaseController {

    @Resource
    protected HttpServletRequest request;
    @Resource
    protected TokenUtils tokenUtils;

    /**
     * 获取当前用户id.
     *
     * @return the string
     */
    public Long currentUserId() {
        String token = request.getHeader(CommonConstants.TOKEN_KEY);
        UserTokenResult result = tokenUtils.parseUser(token, CommonConstants.SYS_LABEL);
        return result.getUserId();
    }

    /**
     * 获取当前用户对象.
     *
     * @return the string
     */
    public UserTokenResult currentUser() {
        String token = request.getHeader(CommonConstants.TOKEN_KEY);
        return tokenUtils.parseUser(token, CommonConstants.SYS_LABEL);
    }

    /**
     * 获取分页参数
     *
     * @param <T>
     * @return
     */
    protected <T> Page<T> getPage() {
        String currentPage = request.getParameter("page");
        String size = request.getParameter("size");
        if (StringUtils.isEmpty(currentPage)) {
            currentPage = "1";
        }
        if (StringUtils.isEmpty(size)) {
            size = "10";
        }
        return new Page(Integer.parseInt(currentPage), Integer.parseInt(size));
    }


}
