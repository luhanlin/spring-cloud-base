package com.luhanlin.feignservice.controller;

import com.luhanlin.common.result.ResultInfo;
import com.luhanlin.feignservice.feign.DemoClient;
import com.luhanlin.feignservice.service.ThreadContextService;
import com.luhanlin.feignservice.threadlocal.UserContextHolder;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类详细描述：服务请求api
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2018/10/18 上午9:38
 */
@Log4j2
@RestController
@RequestMapping("/context")
public class ThreadContextController {

    @Autowired
    private ThreadContextService threadContextService;

    @GetMapping("/{id}")
    public ResultInfo testRest(@PathVariable("id") Integer id){
        log.info("LicenseServiceController Correlation id: {}", UserContextHolder.getContext().getCorrelationId());

        /**
         * LicenseServiceController Correlation id: luhanlin
         * 进入@HystrixCommand注解的方法  Correlation id:
         */
        return threadContextService.testFeign(id);
    }
}
