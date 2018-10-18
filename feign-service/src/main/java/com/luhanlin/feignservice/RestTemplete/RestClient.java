package com.luhanlin.feignservice.RestTemplete;

import com.luhanlin.common.result.ResultInfo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 类详细描述：RestTemplate 请求客户端
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2018/10/17 下午2:11
 */
@Log4j2
@Component
public class RestClient {

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand
    public ResultInfo getTest(Integer id){
        log.info(">>>>>>>>> 进入restTemplate 方法调用 >>>>>>>>>>>>");
        ResponseEntity<ResultInfo> restExchange =
                restTemplate.exchange(
                        "http://demo-service/test/{id}",
                        HttpMethod.GET,
                        null, ResultInfo.class, id);

        return restExchange.getBody();
    }

}
