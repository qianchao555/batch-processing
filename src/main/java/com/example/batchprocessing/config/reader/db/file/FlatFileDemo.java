package com.example.batchprocessing.config.reader.db.file;

import com.example.batchprocessing.config.reader.db.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.BindException;

/**
 * @description:
 * @projectName:batch-processing
 * @see:com.example.batchprocessing.config.reader.db.file
 * @author:xiaoyige
 * @createTime:2021/11/3 21:44
 * @version:1.0
 */
@Configuration
public class FlatFileDemo {
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
        FlatFileItemReader<User> reader=new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("user.txt"));
        reader.setLinesToSkip(1);//跳过第一行
        //开始解析数据
        DelimitedLineTokenizer tokenizer=new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"id","name","address","age","nation"});
        //解析出的数据进行映射
        DefaultLineMapper<User> mapper=new DefaultLineMapper<>();
        mapper.setLineTokenizer(tokenizer);
        mapper.setFieldSetMapper(new FieldSetMapper<User>() {
            @Override
            public User mapFieldSet(FieldSet fieldSet) throws BindException {
                User user=new User();
                user.setId(fieldSet.readInt("id"));
                user.setName(fieldSet.readString("name"));
                user.setAge(fieldSet.readInt("age"));
                user.setAddress(fieldSet.readString("address"));
                user.setNation(fieldSet.readString("nation"));
                return user;
            }
        });
        //检查
        mapper.afterPropertiesSet();
        reader.setLineMapper(mapper);//行映射
        return reader;
    }
}
