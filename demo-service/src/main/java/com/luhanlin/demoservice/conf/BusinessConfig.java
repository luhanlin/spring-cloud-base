package com.luhanlin.demoservice.conf;

import com.luhanlin.common.constant.ResultCode;
import com.luhanlin.common.enums.DataCenterEnum;
import com.luhanlin.common.utils.SnowflakeIdWorker;
import com.luhanlin.dbredis.redis.utils.RedisUtil;
import com.luhanlin.dbredis.redis.utils.RedissLockUtil;
import com.luhanlin.demoservice.conf.prop.CustomBean;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
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

}
