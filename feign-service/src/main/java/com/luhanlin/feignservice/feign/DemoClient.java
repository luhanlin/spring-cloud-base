package com.luhanlin.feignservice.feign;

import com.luhanlin.common.bean.Test;
import com.luhanlin.common.result.ResultInfo;
import com.luhanlin.feignservice.feign.fallback.DemoServiceHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * feign 注解来绑定该接口对应 demo-service 服务
 * name 为其它服务的服务名称
 * fallback 为熔断后的回调
 *
 */
@FeignClient(value = "demo-service",
//        configuration = DisableHystrixConfiguration.class, // 局部关闭断路器
        fallback = DemoServiceHystrix.class)
public interface DemoClient {

    @GetMapping(value = "/test/hello")
    ResultInfo hello();

    @GetMapping(value = "/test/{id}",consumes = "application/json")
    ResultInfo getTest(@PathVariable("id") Integer id);

    @PostMapping(value = "/test/add")
    ResultInfo addTest(Test test);

    @PutMapping(value = "/test/update")
    ResultInfo updateTest(Test test);

    @GetMapping(value = "/test/collapse/{id}")
    Test collapse(@PathVariable("id") Integer id);

    @GetMapping(value = "/test/collapse/findAll")
    List<Test> collapseFindAll(@RequestParam(value = "ids") List<Integer> ids);
}
