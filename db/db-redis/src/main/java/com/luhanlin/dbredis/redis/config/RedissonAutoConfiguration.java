package com.luhanlin.dbredis.redis.config;

import cn.hutool.core.util.StrUtil;
import com.luhanlin.dbredis.redis.config.props.RedissonProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 类详细描述：配置redis
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2018/10/16 上午8:26
 */
@Configuration
@ConditionalOnClass(Config.class)
@RefreshScope
public class RedissonAutoConfiguration {

    @Bean(name="redssionProperties")
    @ConfigurationProperties(prefix="redisson")
    @RefreshScope
    public RedissonProperties druidStatProperties(){
        return new RedissonProperties();
    }

    /**
     * 集群模式自动装配
     * @return
     */
    @Bean
    @ConditionalOnProperty(name="redisson.clusterServersConfig")
    @RefreshScope
    RedissonClient redissonCluster(@Qualifier(value = "redssionProperties")RedissonProperties redssionProperties) {
        Config config = new Config();
        ClusterServersConfig serverConfig = config.useClusterServers()
                .setScanInterval(2000) // 集群状态扫描间隔时间，单位是毫秒
                //可以用"rediss://"来启用SSL连接
                .addNodeAddress(redssionProperties.getNodeAddresses());

        if(StrUtil.isNotBlank(redssionProperties.getPassword())) {
            serverConfig.setPassword(redssionProperties.getPassword());
        }
        return Redisson.create(config);
    }

    /**
     * 哨兵模式自动装配
     * @return
     */
    @Bean
    @ConditionalOnProperty(name="redisson.sentinelServersConfig")
    @RefreshScope
    RedissonClient redissonSentinel(@Qualifier(value = "redssionProperties")RedissonProperties redssionProperties) {
        Config config = new Config();
        SentinelServersConfig serverConfig = config.useSentinelServers().addSentinelAddress(redssionProperties.getSentinelAddresses())
                .setMasterName(redssionProperties.getMasterName())
                .setTimeout(redssionProperties.getTimeout())
                .setMasterConnectionPoolSize(redssionProperties.getMasterConnectionPoolSize())
                .setSlaveConnectionPoolSize(redssionProperties.getSlaveConnectionPoolSize());

        if(StrUtil.isNotBlank(redssionProperties.getPassword())) {
            serverConfig.setPassword(redssionProperties.getPassword());
        }
        return Redisson.create(config);
    }

    /**
     * 单机模式自动装配
     * @return
     */
    @Bean
    @ConditionalOnProperty(name="redisson.singleServerConfig")
    @RefreshScope
    RedissonClient redissonSingle(@Qualifier(value = "redssionProperties")RedissonProperties redssionProperties) {

        Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(redssionProperties.getAddress())
                .setTimeout(redssionProperties.getTimeout())
                .setConnectionPoolSize(redssionProperties.getConnectionPoolSize())
                .setConnectionMinimumIdleSize(redssionProperties.getConnectionMinimumIdleSize());

        if(StrUtil.isNotBlank(redssionProperties.getPassword())) {
            serverConfig.setPassword(redssionProperties.getPassword());
        }

        return Redisson.create(config);
    }

}
