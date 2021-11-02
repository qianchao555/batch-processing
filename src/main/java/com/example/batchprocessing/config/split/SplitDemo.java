package com.example.batchprocessing.config.split;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 * @description:
 * @projectName:batch-processing
 * @see:com.example.batchprocessing.config.split
 * @author:xiaoyige
 * @createTime:2021/11/2 20:50
 * @version:1.0
 */
@Configuration
@EnableBatchProcessing
public class SplitDemo {
    /**
     * 传统step是顺序执行的，split实现并发执行   step/flow在各自的线程中去执行
     */

    @Autowired
    JobBuilderFactory jobBuilderFactory;
    @Autowired
    StepBuilderFactory stepBuilderFactory;

    /**
     * job里面包含flow  flow里面有多个step
     *
     * @return
     */
    @Bean
    public Job splitDemoJob() {
        return jobBuilderFactory.get("splitDemoJob")
                .start(splitDemoFlow1())
                .split(new SimpleAsyncTaskExecutor()).add(splitDemoFlow2()).end()
                .build();

    }

    @Bean
    public Step splitDemoStep1() {
        return stepBuilderFactory.get("splitDemoStep1")
                .tasklet((x, y) -> {
                    System.out.println("splitDemoStep1");
                    return RepeatStatus.FINISHED;
                }).build();

    }

    @Bean
    public Step splitDemoStep2() {
        return stepBuilderFactory.get("splitDemoStep2")
                .tasklet((x, y) -> {
                    System.out.println("splitDemoStep2");
                    return RepeatStatus.FINISHED;
                }).build();

    }

    @Bean
    public Step splitDemoStep3() {
        return stepBuilderFactory.get("splitDemoStep3")
                .tasklet((x, y) -> {
                    System.out.println("splitDemoStep3");
                    return RepeatStatus.FINISHED;
                }).build();

    }

    /**
     * 创建flow对象,包含哪些step，并不是执行
     */
    @Bean
    public Flow splitDemoFlow1() {
        return new FlowBuilder<Flow>("splitDemoFlow1")
                .start(splitDemoStep1())
                .next(splitDemoStep2())
                .build();
    }

    @Bean
    public Flow splitDemoFlow2() {
        return new FlowBuilder<Flow>("splitDemoFlow2")
                .start(splitDemoStep3())
                .build();
    }
}
