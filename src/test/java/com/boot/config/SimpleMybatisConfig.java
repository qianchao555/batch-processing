package com.boot.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @ClassName SimpleMybatisConfig
 * @Author qianchao
 * @Date 2022/9/7
 * @Version  V1.0
 **/
@Configuration
public class SimpleMybatisConfig {
//    @Bean("dataSource")
//    @Primary
//    @ConfigurationProperties(prefix = "datasource")
//    /**
//     * datasource定义 在配置文件中以datasource开始  不是spring.datasource开始
//     */
//    public DataSource getDataSource(){
//
//        return new DruidDataSource();
//    }
}
