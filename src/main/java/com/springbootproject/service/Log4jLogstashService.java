package com.springbootproject.service;

//import com.springbootproject.entity.EsTestEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/6/25 21:47
 * @version:1.0
 */
@Slf4j
@Service
public class Log4jLogstashService {
//    @Autowired
//    ElasticsearchRestTemplate elasticsearchTemplate;
//    public void testEs(){
//        EsTestEntity x = elasticsearchTemplate.get("6EtvoIEB6T4eA9a25Dee", EsTestEntity.class);
//        System.out.println(x.toString());
//
//
//    }
    public void logPrint(){
        log.info("这是20220626Log4jLogstash-method:{}",this.getClass().getMethods());
    }
}
