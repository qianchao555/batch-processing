package com.example.batchprocessing.config.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @description:
 * @projectName:batch-processing
 * @see:com.example.batchprocessing.config.job
 * @author:xiaoyige
 * @createTime:2021/11/2 21:38
 * @version:1.0
 */
@Configuration
public class JobParent {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    Job JobChildOne;
    @Autowired
    Job JobChildTwo;
    @Autowired
    JobLauncher jobLauncher;
    @Autowired
    JobRepository jobRepository;
    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job parentJob() {
        return jobBuilderFactory.get("parentJob")
                .start(childJob1())
                .next(childJob2())
                .build();
    }

    //返回的是Job类型的Step,特殊的Step
    private Step childJob2() {
        return new JobStepBuilder(new StepBuilder("childJob2"))
                .job(JobChildTwo)
                //使用启动对象 =》使用启动父Job的启动对象
                .launcher(jobLauncher)
                .repository(jobRepository)
                .transactionManager(platformTransactionManager)
                .build();
    }

    private Step childJob1() {
        return new JobStepBuilder(new StepBuilder("childJob1")).job(JobChildOne)
                //使用启动对象 =》使用启动父Job的启动对象
                .launcher(jobLauncher)
                .repository(jobRepository)
                .transactionManager(platformTransactionManager)
                .build();
    }

    @Bean
    public Step parentJobStep1() {
        return stepBuilderFactory.get("parentJobStep1").tasklet(
                new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("parentJobStep1");
                        return RepeatStatus.FINISHED;
                    }
                }
        ).build();
    }

    @Bean
    public Step parentJobStep2() {
        return stepBuilderFactory.get("parentJobStep1").tasklet(
                new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("parentJobStep1");
                        return RepeatStatus.FINISHED;
                    }
                }
        ).build();
    }

}
