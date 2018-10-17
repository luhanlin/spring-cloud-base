package com.luhanlin.feignservice.feign;

import com.luhanlin.common.result.ResultInfo;
import com.luhanlin.feignservice.feign.fallback.DemoServiceHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * feign 注解来绑定该接口对应 demo-service 服务
 * name 为其它服务的服务名称
 * fallback 为熔断后的回调
 *
 */
@FeignClient(value = "demo-service", fallback = DemoServiceHystrix.class)
public interface DemoClient {

    @GetMapping(value = "/hello")
    ResultInfo hello();

    @GetMapping(value = "/{id}",consumes = "application/json")
    ResultInfo getTest(@PathVariable("id") Integer id);

}
