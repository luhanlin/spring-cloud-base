package com.luhanlin.dbredis.redis.config.props;

import lombok.Data;


/**
 * 类详细描述：Redisson配置类
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2018/10/16 上午8:26
 */
@Data
public class RedissonProperties {

    private int timeout = 3000;

    private String address;

    private String password;

    private int connectionPoolSize = 64;

    private int connectionMinimumIdleSize=10;

    private int slaveConnectionPoolSize = 250;

    private int masterConnectionPoolSize = 250;

    private String[] sentinelAddresses;

    private String[] nodeAddresses;

    private String masterName;

}
