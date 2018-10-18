package com.luhanlin.feignservice.service;

import com.luhanlin.common.bean.Test;
import com.luhanlin.common.result.ResultInfo;
import com.luhanlin.feignservice.feign.DemoClient;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 类详细描述：缓存service测试类
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2018/10/18 上午12:13
 */

// 类级别属性配置
@DefaultProperties(
    commandProperties={
            // 请求必须达到以下参数以上才有可能触发，也就是10秒內发生连续调用的最小参数
            @HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value="10"),
            // 请求到达requestVolumeThreshold 上限以后，调用失败的请求百分比
            @HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value="75")}
)
@Log4j2
@Service
public class CacheService {

    @Autowired
    private DemoClient demoClient;


    /**
     * commandKey 指定命令名称
     * groupKey 分组
     * threadPoolKey 线程池分组
     *
     * CacheResult 设定请求具有缓存
     *  cacheKeyMethod 指定请求缓存key值设定方法 优先级大于 @CacheKey() 的方式
     *
     * CacheKey() 也是指定缓存key值，优先级较低
     * CacheKey("id") Integer id 出现异常，测试CacheKey()读取对象属性进行key设置
     *  java.beans.IntrospectionException: Method not found: isId
     *
     * 直接使用以下配置会出现异常：
     *   java.lang.IllegalStateException: Request caching is not available. Maybe you need to initialize the HystrixRequestContext?
     *
     * 原因：请求缓存不是只写入一次结果就不再变化的，而是每次请求到达Controller的时候，我们都需要为
     *      HystrixRequestContext进行初始化，之前的缓存也就是不存在了，我们是在同一个请求中保证
     *      结果相同，同一次请求中的第一次访问后对结果进行缓存，缓存的生命周期只有一次请求！
     *      与使用redis进行url缓存的模式不同。
     * 因此，我们需要做过滤器进行HystrixRequestContext初始化。
     *
     */
    @CacheResult(cacheKeyMethod = "getCacheKey")
    @HystrixCommand(commandKey = "testCache", groupKey = "CacheTestGroup", threadPoolKey = "CacheTestThreadPool")
    public ResultInfo testCache(Integer id){
        log.info("test cache 服务调用测试。。。");
        return demoClient.getTest(id);
    }

    /**
     * 这里有两点要特别注意：
     * 1、这个方法的入参的类型必须与缓存方法的入参类型相同，如果不同被调用会报这个方法找不到的异常,
     *    等同于fallbackMethod属性的使用；
     * 2、这个方法的返回值一定是String类型，报出如下异常：
     *    com.netflix.hystrix.contrib.javanica.exception.HystrixCachingException:
     *            return type of cacheKey method must be String.
     */
    private String getCacheKey(Integer id){
        log.info("进入获取缓存key方法。。。");
        return String.valueOf(id);
    }

    @CacheRemove(commandKey = "testCache")
    @HystrixCommand(commandKey = "updateCache", groupKey = "CacheTestGroup", threadPoolKey = "CacheTestThreadPool")
    public ResultInfo updateCache(@CacheKey("id") Test test){
        log.info("update cache 服务调用测试。。。");
        return demoClient.updateTest(test);
    }
}
