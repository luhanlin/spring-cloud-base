package com.luhanlin.common.exception;


import com.luhanlin.common.enums.ResultCodeEnum;
import lombok.Data;

/**
 * 支付公共异常类
 * @author luhanlin
 * @since 2018-05-09
 */
@Data
public class BusinessException extends ResultException {

    private String specificInfo;        // 异常具体信息

    public BusinessException(ResultCodeEnum e) {
        super(e);
    }

    /**
     * 传入具体错误信息构造
     * @param e
     * @param specificInfo
     */
    public BusinessException(ResultCodeEnum e, String specificInfo) {
        super(e);
        this.specificInfo = specificInfo;
    }

}
