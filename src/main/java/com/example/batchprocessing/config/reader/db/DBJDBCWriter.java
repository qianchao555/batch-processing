package com.example.batchprocessing.config.reader.db;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description:
 * @projectName:batch-processing
 * @see:com.example.batchprocessing.config.reader.db
 * @author:xiaoyige
 * @createTime:2021/11/3 21:32
 * @version:1.0
 */
@Component("DBJDBCWriter")
public class DBJDBCWriter implements ItemWriter<User> {
    @Override
    public void write(List<? extends User> list) throws Exception {
        for (User user : list
        ) {
            System.out.println(user);
        }
    }
}
