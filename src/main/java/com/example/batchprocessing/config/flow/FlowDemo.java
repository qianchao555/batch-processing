package com.example.batchprocessing.config.flow;

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

/**
 * @description:
 * @projectName:batch-processing
 * @see:com.example.batchprocessing.config.flow
 * @author:xiaoyige
 * @createTime:2021/11/2 20:30
 * @version:1.0
 */
@Configuration
@EnableBatchProcessing
public class FlowDemo {

    @Autowired
    JobBuilderFactory jobBuilderFactory;
    @Autowired
    StepBuilderFactory stepBuilderFactory;

    /**
     * job里面包含flow  flow里面有多个step
     * @return
     */
    @Bean
    public Job flowDemoJob() {
        return jobBuilderFactory.get("flowDemoJob")
                .start(flowDemoFlow())
                .next(flowDemoStep3())
                .end()
                .build();

    }

    @Bean
    public Step flowDemoStep1() {
        return stepBuilderFactory.get("flowDemoStep1")
                .tasklet((x, y) -> {
                    System.out.println("flowDemoStep1");
                    return RepeatStatus.FINISHED;
                }).build();

    }

    @Bean
    public Step flowDemoStep2() {
        return stepBuilderFactory.get("flowDemoStep2")
                .tasklet((x, y) -> {
                    System.out.println("flowDemoStep2");
                    return RepeatStatus.FINISHED;
                }).build();

    }

    @Bean
    public Step flowDemoStep3() {
        return stepBuilderFactory.get("flowDemoStep3")
                .tasklet((x, y) -> {
                    System.out.println("flowDemoStep3");
                    return RepeatStatus.FINISHED;
                }).build();

    }
    /**
     * 创建flow对象,包含哪些step，并不是执行
     */
    @Bean
    public Flow flowDemoFlow() {
        return new FlowBuilder<Flow>("flowDemoFlow")
                .start(flowDemoStep1())
                .next(flowDemoStep2())
                .build();
    }
}
