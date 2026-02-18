package com.ddzn.dd.model.base;


import com.ddzn.dd.model.enums.ErrorCodeEnum;

/**
 * 业务异常
 * 该异常作为所有项目统一的业务异常, 当出现业务没法继续进行的时候应该抛出这个异常
 *
 * @author zhaopeng
 * @date 2020/06/08 18:00
 */
public class BusinessException extends RuntimeException {
    /**
     * 自定义业务异常码
     */
    protected String code;

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getErrMsg());
        this.code = errorCodeEnum.getErrCode();
    }

    public BusinessException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
