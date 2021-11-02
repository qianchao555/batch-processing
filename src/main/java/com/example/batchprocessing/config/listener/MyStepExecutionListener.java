package com.example.batchprocessing.config.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.context.annotation.Configuration;

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
    @Override
    public void beforeStep(StepExecution stepExecution) {

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
