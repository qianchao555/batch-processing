package com.example.batchprocessing.config.decider;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

/**
 * @description:
 * @projectName:batch-processing
 * @see:com.example.batchprocessing.config.decider
 * @author:xiaoyige
 * @createTime:2021/11/2 21:23
 * @version:1.0
 */
public class MyDecider implements JobExecutionDecider {
    private int count;
    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        count++;
        if(count%2==0){
            //偶数
            return new FlowExecutionStatus("even");
        }else {
            //奇数
            return new FlowExecutionStatus("odd");
        }
    }
}
