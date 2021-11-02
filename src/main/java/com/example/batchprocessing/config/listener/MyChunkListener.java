package com.example.batchprocessing.config.listener;

import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @projectName:batch-processing
 * @see:com.example.batchprocessing.config.listener
 * @author:xiaoyige
 * @createTime:2021/11/2 22:19
 * @version:1.0
 */
@Configuration
public class MyChunkListener {
    /**
     * 注解方式
     */
    @BeforeChunk
    public void beforeChunk(ChunkContext chunkContext){
        System.out.println(chunkContext.getStepContext().getStepName()+"before>>>>>>>>>>>>>>>>>>");

    }
    @AfterChunk
    public void myAfterChunk(ChunkContext chunkContext){
        System.out.println(chunkContext.getStepContext().getStepName()+"after>>>>>>>>>>>>>>>>>>");

    }
}
