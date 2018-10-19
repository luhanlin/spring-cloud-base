package com.luhanlin.zuulgateway.conf;

import com.netflix.zuul.FilterFileManager;
import com.netflix.zuul.FilterLoader;
import com.netflix.zuul.groovy.GroovyCompiler;
import com.netflix.zuul.groovy.GroovyFileFilter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <类详细描述> zuul 过滤配置
 *
 * @author luhanlin
 * @version [V_1.0.0, 2018/7/9 16:33]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Configuration
@RefreshScope
@Log4j2
public class FilterConfiguration {

    @Bean("filterProperties")
    @RefreshScope
    @ConfigurationProperties(prefix="zuul.filter")
    public FilterProperties getFilterConfig() {
        log.info("zuul filter 相关属性配置成功！！！" );
        return new FilterProperties();
    }

    @Bean
    @RefreshScope
    public FilterLoader filterLoader(@Qualifier(value = "filterProperties") FilterProperties filterProperties) {
        log.info("filterLoader 进行校验 filter参数为：" + filterProperties.toString() );
        FilterLoader filterLoader = FilterLoader.getInstance();
        filterLoader.setCompiler(new GroovyCompiler());
        try {
            FilterFileManager.setFilenameFilter(new GroovyFileFilter());
            FilterFileManager.init(
                    filterProperties.getInterval(),
                    filterProperties.getRoot() + "/pre",
                    filterProperties.getRoot() + "/route",
                    filterProperties.getRoot() + "/post",
                    filterProperties.getRoot() + "/error");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return filterLoader;
    }
}
