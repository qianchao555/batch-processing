package com.example.batchprocessing.config.listener;

import org.springframework.batch.core.*;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @description:
 * @projectName:batch-processing
 * @see:com.example.batchprocessing.config.listener
 * @author:xiaoyige
 * @createTime:2021/11/2 22:05
 * @version:1.0
 */
@Configuration
public class MyStepExecutionListener implements StepExecutionListener {
    Map<String, JobParameter> parameters;
    @Override
    public void beforeStep(StepExecution stepExecution) {
         parameters = stepExecution.getJobParameters().getParameters();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
    /**
     * 监听器用来监听批处理作业的执行情况
     * 创建监听器可以通过实现接口或者注解
     */

}
