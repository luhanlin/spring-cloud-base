package com.luhanlin.common.utils;

import com.luhanlin.common.enums.ResultCodeEnum;
import com.luhanlin.common.result.ResultInfo;

public class ResultUtil {

    public static ResultInfo success(Object data) {
        return new ResultInfo<>(ResultCodeEnum.SUCCESS, data);
    }

    public static ResultInfo warn(ResultCodeEnum resultCode, Object data) {
        ResultInfo<Object> result = new ResultInfo<>(resultCode);
        result.setMsg(resultCode.getMsg());
        result.setData(data);
        return result;
    }

    public static ResultInfo warn(ResultCodeEnum resultCode) {
        return new ResultInfo(resultCode);
    }

}
