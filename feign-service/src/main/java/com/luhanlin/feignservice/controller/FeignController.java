package com.luhanlin.feignservice.controller;

import com.luhanlin.common.result.ResultInfo;
import com.luhanlin.common.utils.ResultUtil;
import com.luhanlin.feignservice.RestTemplete.RestClient;
import com.luhanlin.feignservice.feign.DemoClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "/test",tags = "Feign服务调用测试api")
@Log4j2
@RestController
@RequestMapping("/test")
public class FeignController {

    @Autowired
    private DemoClient demoClient;

    @Autowired
    private RestClient restClient;


    @ApiOperation(value = "feign远程服务调用测试",notes = "使用延迟测试服务器的短路由")
    @ApiImplicitParam(name = "id",value = "用户id")
    @HystrixCommand(
            // 开启此项 feign调用的回退处理会直接调用此方法
            fallbackMethod = "buildFallbacktestFeign",
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
    @GetMapping("/feign/{id}")
    public ResultInfo testFeign(@PathVariable("id") Integer id){
        log.info("使用feign进行远程服务调用测试。。。");

        ResultInfo test = demoClient.getTest(id);

        log.info("服务调用获取的数据为： " + test.toString());
        /**
         * hystrix 默认调用超时时间为1秒
         *
         * 此处需要配置 fallbackMethod 属性才会生效
         */
        log.info("服务延时：" + randomlyRunLong() + " 秒");
        return test;
    }

    @HystrixCommand
    @GetMapping("/rest/{id}")
    public ResultInfo testRest(@PathVariable("id") Integer id){
        log.info("使用restTemplate进行远程服务调用测试。。。");
        return restClient.getTest(id);
    }

    /**
     * testFeign 后备方法
     * @return
     */
    private ResultInfo buildFallbacktestFeign(Integer id){
        return ResultUtil.success("testFeign 接口后备处理,参数为： " + id );
    }


    // 模拟服务调用延时
    private Integer randomlyRunLong(){
        Random rand = new Random();

        int randomNum = rand.nextInt(3) + 1;

        if (randomNum==3) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return randomNum;
    }

}
