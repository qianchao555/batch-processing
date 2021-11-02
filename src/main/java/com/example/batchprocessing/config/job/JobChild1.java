package com.example.batchprocessing.config.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @projectName:batch-processing
 * @see:com.example.batchprocessing.config.job
 * @author:xiaoyige
 * @createTime:2021/11/2 21:38
 * @version:1.0
 */
@Configuration
public class JobChild1 {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job JobChildOne(){
        return jobBuilderFactory.get("JobChildOne")
                .start(jobChildStep3())
                .build();
    }

    @Bean
    public Step jobChildStep1() {
        return stepBuilderFactory.get("jobChildStep1").tasklet(
                new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("jobChildStep1");
                        return RepeatStatus.FINISHED;
                    }
                }
        ).build();
    }

    @Bean
    public Step jobChildStep2() {
        return stepBuilderFactory.get("jobChildStep2").tasklet(
                new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("jobChildStep2");
                        return RepeatStatus.FINISHED;
                    }
                }
        ).build();
    }

    @Bean
    public Step jobChildStep3() {
        return stepBuilderFactory.get("jobChildStep3").tasklet(
                new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("jobChildStep3");
                        return RepeatStatus.FINISHED;
                    }
                }
        ).build();
    }
}
