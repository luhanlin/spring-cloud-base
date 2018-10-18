package com.luhanlin.feignservice.controller;

import com.luhanlin.common.result.ResultInfo;
import com.luhanlin.common.utils.ResultUtil;
import com.luhanlin.feignservice.RestTemplete.RestClient;
import com.luhanlin.feignservice.feign.DemoClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * 类详细描述：服务调用对外API
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2018/10/17 下午1:38
 */
@Log4j2
@RestController
@RequestMapping("/exception")
public class ExceptionTestController {

    @Autowired
    private DemoClient demoClient;

    @Autowired
    private RestClient restClient;


    /**
     * ignoreExceptions 属性会将RuntimeException包装
     *   成HystrixBadRequestException,从而不执行回调.
     */
    @HystrixCommand(ignoreExceptions = {RuntimeException.class},
                    fallbackMethod = "buildFallbacktestFeign")
    @GetMapping("/{id}")
    public ResultInfo testException(@PathVariable("id") Integer id){
        log.info("test exception 服务调用异常抛出测试。。。");
        if (id == 1){
            throw new RuntimeException("测试服务调用异常");
        }

        return demoClient.getTest(id);
    }

    /**
     * testFeign 后备方法
     * @return
     */
    private ResultInfo buildFallbackTestException(Integer id){
        return ResultUtil.success("testException 接口后备处理,参数为： " + id );
    }

}
