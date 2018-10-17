package com.luhanlin.common.result;

import com.luhanlin.common.constant.ResultCode;
import com.luhanlin.common.constant.SysCode;
import com.luhanlin.common.enums.ResultCodeEnum;
import lombok.Data;


@Data
public class ResultInfo<T> {

    private String code;
    private String msg;
    private T data;

    public ResultInfo() {
    }

    public ResultInfo(ResultCodeEnum resultCode, T data) {
        this(resultCode);
        this.data = data;
    }
    public ResultInfo(String code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public ResultInfo(ResultCodeEnum resultCode) {
        this.code = ResultCode.SysType + resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

}
