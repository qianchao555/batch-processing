package com.example.batchprocessing.config.writer;

import com.example.batchprocessing.config.reader.db.User;
import com.example.batchprocessing.config.reader.db.file.FlatFileWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

/**
 * @description:
 * @projectName:batch-processing
 * @see:com.example.batchprocessing.config.writer
 * @author:xiaoyige
 * @createTime:2021/11/4 20:10
 * @version:1.0
 */
public class MyWriteDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("FlatFileWriter")
    FlatFileWriter flatFileWriter;

    @Bean
    public Job flatFileJob() {
        return jobBuilderFactory.get("flatFileJob")
                .start(flatFileStep())
                .build();
    }

    @Bean
    public Step flatFileStep() {
        return stepBuilderFactory.get("flatFileStep")
                .<User, User>chunk(3)
                .reader(flatFileRead())
                .writer(flatFileWriter)
                .build();
    }

    @Bean
    public ItemReader<User> flatFileRead() {

        return null;
    }

}
