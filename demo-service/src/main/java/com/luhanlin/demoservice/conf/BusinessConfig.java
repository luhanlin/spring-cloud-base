package com.luhanlin.demoservice.conf;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.luhanlin.common.constant.ResultCode;
import com.luhanlin.common.enums.DataCenterEnum;
import com.luhanlin.common.utils.SnowflakeIdWorker;
import com.luhanlin.dbmysql.props.DruidStatProperties;
import com.luhanlin.dbredis.redis.utils.RedisUtil;
import com.luhanlin.dbredis.redis.utils.RedissLockUtil;
import com.luhanlin.demoservice.conf.prop.CustomBean;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Log4j2
@Configuration
public class BusinessConfig {

    @Bean(name="setSysType")
    @RefreshScope
    public Object setSysType(@Value("${system.type}") String SysType , @Value("${server.port}") String SysPort) throws Exception{
        ResultCode.SysType = SysType;
        ResultCode.SysPort = SysPort;
        log.info(">>>>>> SysType >>>>>>>>>>>>>>>>> "+ ResultCode.SysType);

        SnowflakeIdWorker.datacenterId = DataCenterEnum.getValueByType(ResultCode.SysType);

        InetAddress address = InetAddress.getLocalHost();
        String hostAddress = address.getHostAddress();

        ResultCode.SysIp = hostAddress;
        log.info(">>>>>> hostAddress >>>>>>>>>>>>>>>>> "+ hostAddress);
        String key = "workerId_"+ResultCode.SysType;
        String listKey = "workerId_list_"+ResultCode.SysType;
        boolean tryLock = false;
        try{
            tryLock = RedissLockUtil.tryLock(key, TimeUnit.SECONDS,5,2);
            if(!tryLock){
                throw new Exception("尝试获取锁失败");
            }
            Integer index = null;
            List<String> ips = RedisUtil.getList(listKey);
            int size = 0;
            if(ips!=null && ips.size()>0){
                size = ips.size();
                for(int i=0;i<size;i++){
                    if(ips.get(i).equals(hostAddress)){
                        index = i;
                        break;
                    }
                }
            }
            if(index==null){
                RedisUtil.rPush(listKey,hostAddress);
                index = size;
            }
            SnowflakeIdWorker.workerId = index;
        }finally {
            if(tryLock){
                RedissLockUtil.unlock(key);
            }
        }

        log.info(">>>>>> SnowflakeIdWorker.nextId() >>>>>>>>>>>>>>>>> "+ SnowflakeIdWorker.nextId());
        return ResultCode.SysType;
    }

    @Bean
    @ConfigurationProperties(prefix="custom.bean")
    public CustomBean initCustomBean(){
        log.info(">>>>>>>>>>> CustomBean >>>>>>>>>>>>>>> 配置成功!!!");
        return new CustomBean();
    }

    @Bean
    public ServletRegistrationBean getServlet() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new HystrixMetricsStreamServlet());
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
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
        System.out.println("》》》》》》  开始进行druid控制台配置  》》》》》》》》》》" + druidStatProperties);
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

    @Bean(name="druidStatProperties")
    @ConfigurationProperties(prefix="druid.stat")
    public DruidStatProperties druidStatProperties(){
        return new DruidStatProperties();
    }



}
