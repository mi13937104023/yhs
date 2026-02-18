package com.ddzn.dd.model.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 统一的web返回对象格式
 *
 * @param <T> 返回数据类型
 */
@Data
@ApiModel("返回类")
public class ResponseResult<T> {
    public static final int SUCCESS_CODE = 200;
    public static final int FAILED_CODE = 500;
    public static final int EXCEPTION_CODE = -1;
    public static final int PARAM_VALID_FAILED_CODE = -2;
    public static final int NO_AUTH = 401;

    public static final String SUCCESS_MSG = "请求成功";
    public static final String FAILED_MSG = "请求失败";
    public static final String EXCEPTION_MSG = "程序异常";

    public static final int SUCCESS_RET = 1;
    public static final int FAIL_RET = -1;

    @ApiModelProperty("返回code码")
    private int code;

    @ApiModelProperty("返回信息")
    private String msg;

    @ApiModelProperty("返回数据对象")
    private T data;

    private Exception ex;

    private int ret;

    public ResponseResult() {
    }

    public ResponseResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.ret = code == SUCCESS_CODE ? SUCCESS_RET : FAIL_RET;
    }

    public ResponseResult(int code, String msg, T data) {
        this(code, msg);
        this.data = data;
    }

    public ResponseResult(int code, String msg, T data, Exception ex) {
        this(code, msg, data);
        this.ex = ex;
    }

    // ----------- 成功 ------------

    public static <T> ResponseResult<T> success() {
        return new ResponseResult<>(SUCCESS_CODE, SUCCESS_MSG, null);
    }

    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(SUCCESS_CODE, SUCCESS_MSG, data);
    }

    public static <T> ResponseResult<T> success(String msg, T data) {
        return new ResponseResult<>(SUCCESS_CODE, msg, data);
    }

    /**
     * 分页成功（自动包装成IPage）
     */
    public static <T> ResponseResult<IPage<T>> pageSuccess(List<T> data) {
        IPage<T> page = new Page<>();
        page.setRecords(data);
        page.setTotal(data.size());
        page.setCurrent(1);
        page.setPages(1);
        return success(page);
    }

    // ----------- 失败 ------------

    public static <T> ResponseResult<T> failed() {
        return new ResponseResult<>(FAILED_CODE, FAILED_MSG, null);
    }

    public static <T> ResponseResult<T> failed(String msg) {
        return new ResponseResult<>(FAILED_CODE, msg, null);
    }

    public static <T> ResponseResult<T> failed(int code, String msg) {
        return new ResponseResult<>(code, msg, null);
    }

    public static <T> ResponseResult<T> failed(String msg, T data) {
        return new ResponseResult<>(FAILED_CODE, msg, data);
    }

    // ----------- 异常 ------------

    public static <T> ResponseResult<T> exception() {
        return new ResponseResult<>(EXCEPTION_CODE, EXCEPTION_MSG, null);
    }

    public static <T> ResponseResult<T> exception(String msg) {
        return new ResponseResult<>(EXCEPTION_CODE, msg, null);
    }

    public static <T> ResponseResult<T> exception(Exception e) {
        return new ResponseResult<>(EXCEPTION_CODE, EXCEPTION_MSG, null, e);
    }

    public static <T> ResponseResult<T> exception(Exception e, String msg) {
        return new ResponseResult<>(EXCEPTION_CODE, msg, null, e);
    }

    // ----------- 参数校验失败 ------------

    /**
     * 请求参数校验失败统一返回方法, 这里暂定参数错误统一的错误码为-2
     *
     * @param msg 失败参数具体错误信息
     * @return 返回标准的报文对象
     */
    public static ResponseResult paramValid(String msg) {
        return new ResponseResult(FAILED_CODE, msg);
    }

}
