package com.luhanlin.zuulgateway.conf;

import com.luhanlin.zuulgateway.filter.post.UrlCachePostFilter;
import com.luhanlin.zuulgateway.filter.pre.UrlCacheProFilter;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <类详细描述> 配置类
 *
 * @author luhanlin
 * @version [V_1.0.0, 2018/7/24 18:41]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Configuration
@RefreshScope
@Log4j2
public class FilterConf {

    @Bean
    @RefreshScope
    public UrlCacheProFilter getUrlCacheProFilter() {
        log.info("zuul UrlCacheProFilter 相关属性配置成功！！！" );
        return new UrlCacheProFilter();
    }

    @Bean
    @RefreshScope
    public UrlCachePostFilter getUrlCachePostFilter() {
        log.info("zuul UrlCachePostFilter 相关属性配置成功！！！" );
        return new UrlCachePostFilter();
    }

}
