package com.luhanlin.feignservice.feign.fallback;

import com.luhanlin.common.enums.ResultCodeEnum;
import com.luhanlin.common.result.ResultInfo;
import com.luhanlin.common.utils.ResultUtil;
import com.luhanlin.feignservice.feign.DemoClient;
import lombok.extern.apachecommons.CommonsLog;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * 类详细描述：demo 服务远程调用后备类
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2018/10/17 下午1:32
 */
@Log4j2
@Component
public class DemoServiceHystrix implements DemoClient {
    @Override
    public ResultInfo hello() {
        log.info("DEMO 服务调用 hello() 方法失败");
        return ResultUtil.warn(ResultCodeEnum.BusinessInvokeServiceFailed,"DEMO 服务调用 hello() 方法失败");
    }

    @Override
    public ResultInfo getTest(Integer id) {
        log.info("DEMO 服务调用 getTest() 方法失败");
        return ResultUtil.warn(ResultCodeEnum.BusinessInvokeServiceFailed,"DEMO 服务调用 getTest() 方法失败");
    }
}
