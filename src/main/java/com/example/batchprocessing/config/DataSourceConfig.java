package com.example.batchprocessing.config;

import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @description:
 * @projectName:batch-processing
 * @see:com.example.batchprocessing.config
 * @author:xiaoyige
 * @createTime:2021/11/1 21:31
 * @version:1.0
 */
@Configuration
public class DataSourceConfig {
    @Bean
    public JobRepositoryFactoryBean jobRepositoryFactoryBean(DataSource dataSource, PlatformTransactionManager platformTransactionManager){
        JobRepositoryFactoryBean jobRepositoryFactoryBean=new JobRepositoryFactoryBean();
        jobRepositoryFactoryBean.setTransactionManager(platformTransactionManager);
        jobRepositoryFactoryBean.setDataSource(dataSource);
        try {
            jobRepositoryFactoryBean.afterPropertiesSet();
        } catch (Exception e) {


        }
        return jobRepositoryFactoryBean;
    }
}
