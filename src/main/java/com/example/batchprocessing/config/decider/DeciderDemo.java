package com.example.batchprocessing.config.decider;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @projectName:batch-processing
 * @see:com.example.batchprocessing.config.decider
 * @author:xiaoyige
 * @createTime:2021/11/2 21:19
 * @version:1.0
 */
@Configuration
@EnableBatchProcessing
public class DeciderDemo {
    /**
     * 决策器的使用
     */
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job deciderJob(){
        return jobBuilderFactory.get("deciderJob")
                .start(deciderStep1())
                //决策器
                .next(myDecider())
                .from(myDecider()).on("even").to(deciderStep2())
                .from(myDecider()).on("odd").to(deciderStep3())
                //执行3之后，无论返回什么，返回至决策器
                .from(deciderStep3()).on("*").to(myDecider())
                .end()
                .build();
    }

    @Bean
    public Step deciderStep1() {
        return stepBuilderFactory.get("deciderStep1").tasklet(
                new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("deciderStep1");
                        return RepeatStatus.FINISHED;
                    }
                }
        ).build();
    }

    @Bean
    public Step deciderStep2() {
        return stepBuilderFactory.get("deciderStep2").tasklet(
                new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("deciderStep2---even----偶数----");
                        return RepeatStatus.FINISHED;
                    }
                }
        ).build();
    }

    @Bean
    public Step deciderStep3() {
        return stepBuilderFactory.get("deciderStep3").tasklet(
                new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("deciderStep3---even----奇数----");
                        return RepeatStatus.FINISHED;
                    }
                }
        ).build();
    }

    //创建决策器
    @Bean
    public JobExecutionDecider myDecider(){
        return new MyDecider();
    }
}
