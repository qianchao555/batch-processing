package com.example.batchprocessing.config.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.Iterator;
import java.util.List;

/**
 * @description:
 * @projectName:batch-processing
 * @see:com.example.batchprocessing.config.reader
 * @author:xiaoyige
 * @createTime:2021/11/3 20:48
 * @version:1.0
 */
public class MyReader implements ItemReader<String> {
    Iterator<String> iterator;
    public MyReader(List<String> list){
        this.iterator=list.iterator();
    }
    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        //一个数据一个数据的读
        if(iterator.hasNext()){
            return iterator.next();
        }else {
            return null;
        }
    }
}
