package com.example.batchprocessing.config.listener;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @projectName:batch-processing
 * @see:com.example.batchprocessing.config.listener
 * @author:xiaoyige
 * @createTime:2021/11/2 22:22
 * @version:1.0
 */
@Configuration
public class ListenerDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    MyJobExecutionListener myJobExecutionListener;

    @Bean
    public Job listenerJob() {
        return jobBuilderFactory.get("listenerJob")
                .start(step1())
                .listener(myJobExecutionListener)
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<String, String>chunk(2) //read、process、write
                .faultTolerant()
                .listener(new MyChunkListener())
                .reader(read())
                .writer(write())
                .build();
    }

    @Bean
    public ItemWriter<String> write() {
        ItemWriter<String> itemWriter = new ItemWriter<String>() {
            @Override
            public void write(List<? extends String> list) throws Exception {
                for (String item:list) {
                    System.out.println(item);
                }
            }
        };
        return itemWriter;
    }

    @Bean
    public ItemReader<String> read() {
        List<String> list = Arrays.asList("124", "125", "126");
        ListItemReader<String> listItemReader = new ListItemReader<>(list);
        return listItemReader;
    }
}
