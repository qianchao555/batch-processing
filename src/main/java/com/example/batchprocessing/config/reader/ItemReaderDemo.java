package com.example.batchprocessing.config.reader;

import com.example.batchprocessing.config.listener.MyStepExecutionListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @projectName:batch-processing
 * @see:com.example.batchprocessing.config.reader
 * @author:xiaoyige
 * @createTime:2021/11/3 20:43
 * @version:1.0
 */
@Configuration
public class ItemReaderDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    MyStepExecutionListener myStepExecutionListener;

    @Bean
    public Job itemReaderJob() {
        return jobBuilderFactory.get("itemReaderJob")
                .start(itemReaderStep1())
                .build();
    }

    @Bean
    public Step itemReaderStep1() {
        return stepBuilderFactory.get("itemReaderStep1")
                .listener(myStepExecutionListener)
                .<String, String>chunk(2)
                .reader(itemReaderRead())
                .writer(System.out::println)
                .build();
    }

    @Bean
    public MyReader itemReaderRead() {
        List<String> list = Arrays.asList("cat", "dog", "pig");
        return new MyReader(list);
    }
}
