package com.luhanlin.dbmysql;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.luhanlin.common.utils.BlankUtil;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * MybatisPlus 相关配置
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2018/10/15 14:50]
 */
@Configuration
@RefreshScope
@MapperScan("com.zgshfpw.*.dao")
@AutoConfigureAfter(DataSourceConfig.class)
public class MybatisPlusConfig {

    private static Logger log = LogManager.getLogger(MybatisPlusConfig.class);
    /*
     * 分页插件，自动识别数据库类型
     * 多租户，请参考官网【插件扩展】
     */
    @Bean
    @RefreshScope
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }


    //    mybatisPlus全局配置
    @Bean(name = "basisGlobalConfig")
    @ConfigurationProperties(prefix="mybatis-plus")
    @RefreshScope
    public GlobalConfiguration globalConfig(
            @Value("${mybatis-plus.global-config.id-type}") Integer idType, //主键类型  0:"数据库ID自增", 1:"用户输入ID",2:"全局唯一ID (数字类型唯一ID)", 3:"全局唯一ID UUID";
            @Value("${mybatis-plus.global-config.db-column-underline}") Boolean dbColumnUnderline, //驼峰下划线转换
            @Value("${mybatis-plus.global-config.capital-mode}") Boolean isCapitalMode //数据库大写下划线转换
    ) {
        log.info("初始化GlobalConfiguration");
        GlobalConfiguration globalConfig = new GlobalConfiguration();
        // 主键类型
        if ( !BlankUtil.isBlank(idType)) {
            globalConfig.setIdType(idType);
        }
        // 驼峰下划线转换
        if ( !BlankUtil.isBlank(dbColumnUnderline)) {
            globalConfig.setDbColumnUnderline(dbColumnUnderline);
        }
        // 数据库大写下划线转换
        if ( !BlankUtil.isBlank(isCapitalMode)) {
            globalConfig.setCapitalMode(isCapitalMode);
        }
        return globalConfig;
    }

    @Value("${mybatis-plus.mapper-locations}")
    private String mapperLocations;

    @Value("${mybatis-plus.typeAliasesPackage}")
    private String typeAliasesPackage;

    @Bean(name = "basisSqlSessionFactory")
    @RefreshScope
    public SqlSessionFactory sqlSessionFactory(@Qualifier(value = "basisGlobalConfig")GlobalConfiguration globalConfig,
                                               @Qualifier(value = "dataSource")DruidDataSource dataSource) throws Exception {
        log.info("初始化SqlSessionFactory");
//        String mapperLocations = "classpath:db-ason/sql/**/*.xml";
//        String configLocation = "classpath:db-ason/mybatis/mybatis-sqlconfig.xml";
//        String typeAliasesPackage = "com.ason.entity.**";
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        // 数据源
        sqlSessionFactory.setDataSource(dataSource);
        // 全局配置
        sqlSessionFactory.setGlobalConfig(globalConfig);
        Interceptor[] interceptor = {new PaginationInterceptor()};
        // 分页插件
        sqlSessionFactory.setPlugins(interceptor);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            // 自动扫描Mapping.xml文件
            sqlSessionFactory.setMapperLocations(resolver.getResources(mapperLocations));
//            sqlSessionFactory.setConfigLocation(resolver.getResource(configLocation));
            sqlSessionFactory.setTypeAliasesPackage(typeAliasesPackage);
            return sqlSessionFactory.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier(value = "basisSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}