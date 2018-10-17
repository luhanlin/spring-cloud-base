package com.luhanlin.dbmysql;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.luhanlin.dbmysql.props.DruidStatProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * 数据源 相关配置
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2018/10/15 14:50]
 */
@Configuration
@RefreshScope
public class DataSourceConfig {

    @Bean(name="dataSource")
    @ConfigurationProperties(prefix="spring.datasource")
    public DruidDataSource dataSource(){
        System.out.println("》》》》》》  开始进行数据源配置  》》》》》》》》》》");
        return new DruidDataSource();
    }

    // 配置事物管理器
    @Bean(name="transactionManager")
    public DataSourceTransactionManager transactionManager(){
        return new DataSourceTransactionManager(dataSource());
    }


    @Bean(name="druidStatProperties")
    @ConfigurationProperties(prefix="druid.stat")
    public DruidStatProperties druidStatProperties(){
        return new DruidStatProperties();
    }


    /**
     * 配置监控服务器
     *
     * @return 返回监控注册的servlet对象
     */
    @Bean
    @ConditionalOnProperty(name="druid.stat.loginUsername")
    public ServletRegistrationBean statViewServlet(@Qualifier(value = "druidStatProperties")DruidStatProperties druidStatProperties) {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        System.out.println("》》》》》》  开始进行druid控制台配置  》》》》》》》》》》");
        if(StrUtil.isNotBlank(druidStatProperties.getAllow())) {
            // 添加IP白名单
            servletRegistrationBean.addInitParameter("allow", druidStatProperties.getAllow());
        }

        if(StrUtil.isNotBlank(druidStatProperties.getDeny())) {
            // 添加IP黑名单，当白名单和黑名单重复时，黑名单优先级更高
            servletRegistrationBean.addInitParameter("deny", druidStatProperties.getDeny());
        }
        // 添加控制台管理用户
        servletRegistrationBean.addInitParameter("loginUsername", druidStatProperties.getLoginUsername());
        servletRegistrationBean.addInitParameter("loginPassword", druidStatProperties.getLoginPassword());
        // 是否能够重置数据
        servletRegistrationBean.addInitParameter("resetEnable", druidStatProperties.getResetEnable());
        return servletRegistrationBean;
    }

    /**
     * 配置服务过滤器
     *
     * @return 返回过滤器配置对象
     */
    @Bean
    @ConditionalOnProperty(name="druid.stat.loginUsername")
    public FilterRegistrationBean statFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        // 添加过滤规则
        filterRegistrationBean.addUrlPatterns("/*");
        // 忽略过滤格式
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*,");
        return filterRegistrationBean;
    }

}
