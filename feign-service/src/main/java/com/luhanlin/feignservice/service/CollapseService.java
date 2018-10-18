package com.luhanlin.feignservice.service;

import com.luhanlin.common.bean.Test;
import com.luhanlin.feignservice.feign.DemoClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类详细描述：测试请求合并
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2018/10/18 上午10:28
 */
@Log4j2
@Service
public class CollapseService {

    @Autowired
    private DemoClient demoClient;

    @HystrixCollapser(
            // 指定请求合并的batch方法
            batchMethod = "findAll",
            collapserProperties = {
                    // 请求合并时间窗为 100ms ，需要根据请求的延迟等进行综合判断进行设置
                    @HystrixProperty(name = "timerDelayInMilliseconds", value = "1000")
            })
    public Test testRest(Integer id){
        Test test = demoClient.collapse(id);
        return test;
    }

    // batch method must be annotated with HystrixCommand annotation
    @HystrixCommand
    private List<Test> findAll(List<Integer> ids){
        log.info("使用 findAll 进行远程服务 Collapse 调用测试。。。");

        return demoClient.collapseFindAll(ids);
    }
}
