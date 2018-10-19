package com.luhanlin.zuulgateway.conf;

import lombok.Data;

/**
 * <类详细描述> 自定义zuul过滤配置文件接收类
 *
 * @author luhanlin
 * @version [V_1.0.0, 2018/7/9 16:31]
 */
@Data
public class FilterProperties {

    private String root;
    private Integer interval;
}
