package com.luhanlin.feignservice;

import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@ServletComponentScan
@EnableFeignClients
@SpringCloudApplication
public class FeignServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignServiceApplication.class, args);
    }

    /**
     * 设置feign远程调用日志级别
     * Logger.Level有如下几种选择：
     * NONE, 不记录日志 (默认)。
     * BASIC, 只记录请求方法和URL以及响应状态代码和执行时间。
     * HEADERS, 记录请求和应答的头的基本信息。
     * FULL, 记录请求和响应的头信息，正文和元数据。
     *
     */
    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.HEADERS;
    }

    /**
     * 引入restTemplate负载均衡
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
