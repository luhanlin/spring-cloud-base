package com.luhanlin.feignservice.service;

import com.luhanlin.common.result.ResultInfo;
import com.luhanlin.feignservice.feign.DemoClient;
import com.luhanlin.feignservice.threadlocal.UserContextHolder;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 类详细描述：
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2018/10/18 下午12:35
 */
@Log4j2
@Service
public class ThreadContextService {

    @Autowired
    private DemoClient demoClient;

    @HystrixCommand(
            // 开启此项 feign调用的回退处理会直接调用此方法
//            fallbackMethod = "buildFallbacktestFeign",
            // 以下为 舱壁模式配置配置单独的线程池
            threadPoolKey = "test",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize",value="30"),
                    @HystrixProperty(name="maxQueueSize", value="10")},
            // 以下为断路器相关配置 可以根据系统对参数进行微调
            commandProperties={
                    // 设置hystrix远程服务调用超时时间，一般不建议修改
//                    @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="4000"),
                    // 请求必须达到以下参数以上才有可能触发，也就是10秒內发生连续调用的最小参数
                    @HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value="10"),
                    // 请求到达requestVolumeThreshold 上限以后，调用失败的请求百分比
                    @HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value="75"),
                    // 断路由半开后进入休眠的时间，期间可以允许少量服务通过
                    @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value="7000"),
                    // 断路器监控时间 默认10000ms
                    @HystrixProperty(name="metrics.rollingStats.timeInMilliseconds", value="15000"),
                    // timeInMilliseconds的整数倍，此处设置越高，cpu占用资源越多我
                    @HystrixProperty(name="metrics.rollingStats.numBuckets", value="5")}
    )
    public ResultInfo testFeign(Integer id){
        log.info("进入@HystrixCommand注解的方法  Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        return demoClient.getTest(id);
    }
}
