package com.example.batchprocessing.config.jobparameters;

import com.example.batchprocessing.config.listener.MyStepExecutionListener;
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
 * @see:com.example.batchprocessing.config.jobparameters
 * @author:xiaoyige
 * @createTime:2021/11/3 20:08
 * @version:1.0
 */
@Configuration
public class ParameterDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    MyStepExecutionListener myStepExecutionListener;

    @Bean
    public Job parameterJob() {
        return jobBuilderFactory.get("parameterJob")
                .start(parameterStep1())
                .build();
    }

    //Job执行的step,Job使用的数据肯定是在Step中使用==》只需要给Step传递数据
    //1、使用监听方式、使用step级别的监听器来传递数据
    @Bean
    public Step parameterStep1() {
        return stepBuilderFactory.get("parameterStep1")
                .listener(myStepExecutionListener)
                .tasklet(
                        new Tasklet() {
                            @Override
                            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                                System.out.println("这是----parameterStep1---------------");
                                System.out.println(stepContribution.getStepExecution().getJobParameters().getParameters().get("info"));
                                return RepeatStatus.FINISHED;
                            }
                        }
                ).build();
    }
}
