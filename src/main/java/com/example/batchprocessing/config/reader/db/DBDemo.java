package com.example.batchprocessing.config.reader.db;

import com.example.batchprocessing.config.listener.MyStepExecutionListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @projectName:batch-processing
 * @see:com.example.batchprocessing.config.reader.db
 * @author:xiaoyige
 * @createTime:2021/11/3 21:10
 * @version:1.0
 */
@Configuration
public class DBDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    MyStepExecutionListener myStepExecutionListener;

    @Autowired
    DataSource dataSource;

    @Autowired
    @Qualifier("DBJDBCWriter")
    private DBJDBCWriter dbjdbcWriter;

    @Bean
    public Job itemReaderDBJob1() {
        return jobBuilderFactory.get("itemReaderDBJob1")
                .start(itemReaderDBStep1())
                .build();
    }

    @Bean
    public Step itemReaderDBStep1() {
        return stepBuilderFactory.get("itemReaderDBStep1")
                .listener(myStepExecutionListener)
                .<User, User>chunk(2)
                .reader(itemReaderReadDB())
                .writer(dbjdbcWriter)
                .build();
    }

    @Bean
    public JdbcPagingItemReader<User> itemReaderReadDB(){
        JdbcPagingItemReader<User> userJdbcPagingItemReader = new JdbcPagingItemReader<>();
        userJdbcPagingItemReader.setDataSource(dataSource);
        userJdbcPagingItemReader.setFetchSize(2);//取两个
        //读取到的记录 转换未User
        userJdbcPagingItemReader.setRowMapper(new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user=new User();
                user.setId(rs.getInt(1));
                user.setAddress(rs.getString(2));
                user.setAge(rs.getInt(3));
                user.setName(rs.getString(4));
                user.setNation(rs.getString(5));
                return user;
            }
        });
        //指定SQL语句
        MySqlPagingQueryProvider query =new MySqlPagingQueryProvider();
        query.setSelectClause("id,address,age,name,nation");
        query.setFromClause("from user");
        //根据那个字段进行排序
        Map<String, Order> sortMap=new HashMap<>();
        sortMap.put("id",Order.ASCENDING);
        query.setSortKeys(sortMap);

        userJdbcPagingItemReader.setQueryProvider(query);
        return userJdbcPagingItemReader;
    }
}
