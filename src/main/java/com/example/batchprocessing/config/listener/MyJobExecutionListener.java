package com.example.batchprocessing.config.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @projectName:batch-processing
 * @see:com.example.batchprocessing.config.listener
 * @author:xiaoyige
 * @createTime:2021/11/2 22:10
 * @version:1.0
 */
@Configuration
public class MyJobExecutionListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println(jobExecution.getJobInstance().getJobName()+"before--------------------");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println(jobExecution.getJobInstance().getJobName()+"after--------------------");

    }
}
