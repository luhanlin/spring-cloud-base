package com.luhanlin.feignservice.controller;

import com.luhanlin.common.bean.Test;
import com.luhanlin.common.result.ResultInfo;
import com.luhanlin.common.utils.ResultUtil;
import com.luhanlin.feignservice.feign.DemoClient;
import com.luhanlin.feignservice.service.CacheService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
@RequestMapping("/collapse")
public class CollapseController {

    @Autowired
    private DemoClient demoClient;

    @HystrixCollapser(
            // 指定请求合并的batch方法
            batchMethod = "findAll",
            collapserProperties = {
            // 请求合并时间窗为 100ms ，需要根据请求的延迟等进行综合判断进行设置
            @HystrixProperty(name = "timerDelayInMilliseconds", value = "1000")
            })
    @GetMapping("/{id}")
    public ResultInfo testRest(@PathVariable("id") Integer id){
        log.info("进行 Collapse 远程服务调用测试,开始时间： " + LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE));
        Test test = demoClient.collapse(id);
        log.info("进行 Collapse 远程服务调用测试,结束时间： " + LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE));
        return ResultUtil.success(test);
    }

    private List<Test> findAll(List<Integer> ids){
        // 直接return 测试
        log.info("使用 findAll 进行远程服务 Collapse 调用测试。。。");

        return demoClient.collapseFindAll(ids);
    }

}
