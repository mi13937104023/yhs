package com.ddzn.dd.framework.common.exception;

import com.ddzn.dd.model.base.BusinessException;
import com.ddzn.dd.model.base.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 全局的异常处理器
 * <p>
 * 为什么需要这个全局的异常处理器?
 * 1.我们使用注解对请求参数校验失败的时候, 返回的报文并非我们想要的格式
 * 2.以前我们的程序抛出异常后需要手动try...catch然后取出错误信息生成标准的响应报文, 这样存在代码冗余
 * 3.出现异常了我们只关心是否能返回友好的提示信息给客户端, 至于异常信息怎么处理我们不需要关心，因为这不是业务代码需要关心的
 *
 * @author zhaopeng
 */
@RestControllerAdvice
@Slf4j
public class GlobalExcepionsHandler {

    /**
     * 用来处理方法请求参数校验异常, 生成统一的响应报文
     *
     * @param request   请求对象
     * @param exception 异常对象
     * @return 响应报文对象
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseResult handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException exception) {
        StringBuilder sb = new StringBuilder();
        List<FieldError> fieldErrorList = exception.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrorList) {
            if (fieldError != null && fieldError.getDefaultMessage() != null) {
                sb.append(fieldError.getDefaultMessage());
                sb.append(",");
            }
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return ResponseResult.paramValid(sb.toString());
    }

    @ExceptionHandler(value = BindException.class)
    public ResponseResult bindException(HttpServletRequest request, BindException exception) {
        List<ObjectError> allErrors = exception.getAllErrors();
        ObjectError objectError = allErrors.get(0);
        return ResponseResult.paramValid(objectError.getDefaultMessage());
    }

    /**
     * 处理业务异常, 生成统一的响应报文
     *
     * @param request 请求对象
     * @return 响应报文
     */
    @ExceptionHandler(value = BusinessException.class)
    public ResponseResult handleBusinessException(HttpServletRequest request, BusinessException exception) {
        return ResponseResult.failed(StringUtils.isEmpty(exception.getCode()) ? ResponseResult.FAILED_CODE : Integer.parseInt(exception.getCode()), exception.getMessage());
    }

    /**
     * 处理异常超类, 生成统一的响应报文
     *
     * @param request   请求对象
     * @param exception 异常超类
     * @return 响应报文对象
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseResult handleException(HttpServletRequest request, Exception exception) {
        if (exception.getMessage().contains("java.io.IOException: Broken pipe")) {
            return ResponseResult.exception("java.io.IOException: Broken pipe");
        } else if (exception.getMessage().contains("java.io.EOFException: Unexpected EOF read on the socket")) {
            return ResponseResult.exception("java.io.EOFException: Unexpected EOF read on the socket");
        } else {
            log.error("GlobalExcepionsHandler handleException:{},request:{}", exception.getMessage(), request.getRequestURI(), exception);
            return ResponseResult.exception("未知异常");
        }
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseResult handleException(HttpServletRequest request, RuntimeException exception) {
        log.error("GlobalExcepionsHandler runtimeException:{},request:{}", exception.getMessage(), request.getRequestURI(), exception);
        throw exception;
    }

}
