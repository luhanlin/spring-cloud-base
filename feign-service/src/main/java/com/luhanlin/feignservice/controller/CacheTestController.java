package com.luhanlin.feignservice.controller;

import com.luhanlin.common.bean.Test;
import com.luhanlin.common.result.ResultInfo;
import com.luhanlin.common.utils.ResultUtil;
import com.luhanlin.feignservice.RestTemplete.RestClient;
import com.luhanlin.feignservice.feign.DemoClient;
import com.luhanlin.feignservice.service.CacheService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/cache")
public class CacheTestController {

    @Autowired
    private CacheService cacheService;

    @GetMapping("/{id}/{name}")
    public ResultInfo testCache(@PathVariable("id") Integer id,@PathVariable("name") String name){
        // 查询缓存数据
        log.info("第一次查询： "+cacheService.testCache(id));
        // 再次查询 查看日志是否走缓存
        log.info("第二次查询： "+cacheService.testCache(id));

        // 更新数据
        cacheService.updateCache(new Test(id,name,"121"));
        // 再次查询 查看日志是否走缓存,不走缓存则再次缓存
        log.info("第二次查询： "+cacheService.testCache(id));
        // 再次查询 查看日志是否走缓存
        log.info("第二次查询： "+cacheService.testCache(id));

        return ResultUtil.success("cache 测试完毕！！！");
    }

}
