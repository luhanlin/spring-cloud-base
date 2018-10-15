package com.luhanlin.common.exception;


import com.luhanlin.common.enums.ResultCodeEnum;

/**
 * 结果异常，会被 ExceptionHandler 捕捉并返回给前端
 *
 */
public class ResultException extends RuntimeException {

    private ResultCodeEnum resultCode;

    public ResultException() {
        super();
        this.resultCode = null;
    }

    public ResultException(ResultCodeEnum resultCode) {
        super(resultCode.getMsg());
        this.resultCode = resultCode;
    }

    public ResultCodeEnum getResultCode() {
        return resultCode;
    }
}
