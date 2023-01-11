package com.worksummary.year2022;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @Description:Java多线程,自动任务scheduled调用存储过程, 其中：存储过程的名字，存放在一张数据库配置表中，可以配置某个存储过程是否去跑（配置一个字段来开启、关闭）
 * 表的设计：若多个存储过程去操作配置表时候，造成锁表情况，则需要把各个配置项目分开存放在一个表中，再搞一个总的汇总表
 * @ClassName JavaCallStoredProcedure
 * @Author qianchao
 * @Date 2023/1/11
 * @Version V1.0
 **/

@Slf4j
public class JavaCallStoredProcedure {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 每2小时跑一次
     */
    @Scheduled(cron = "0 0 0/2 * * ?")
    public void scheduleTask() {
        //引入线程池，是否需要多线程：根据自身业务而定，存储过程要同时启动则拿多线程来启动，不用同时启动则不需要多线程来跑
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(20);


        //k8s部署时候，多个pod同时启动时，只有一个pod能执行
        RLock lock = redissonClient.getLock("标识符1:标识符2:" + redissonClient.getId());
        boolean isLockObtained = false;
        try {
            isLockObtained = lock.tryLock();

            //pod没获取到锁，直接返回,啥也不干
            if (!isLockObtained) {
                log.info("锁获取失败，此次操作xxxx结束。");
                return;
            }

            //根据总表中的表名查询：状态开启的记录
            List<FunctionTask> functionTasks = new ArrayList<>();

            //任务数量
            int functionTaskSize = functionTasks.size();


            //拿20个线程来跑、一个线程跑一个任务（存储过程），具体多少线程来跑，需要看任务的多少
            for (int i = 0; i < functionTaskSize; i++) {
                final int index = i;
                scheduledThreadPool.execute(
                        () -> callFunctionTask(functionTasks.get(index))
                );
            }
        } catch (Exception e) {
            log.error("调用存储过程出错：", e.getMessage());
        } finally {
            if (isLockObtained) {
                lock.unlock();
            }
        }
    }

    /**
     * 调用存储过程
     *
     * @param functionTask
     */
    private void callFunctionTask(FunctionTask functionTask) {
        if (functionTask == null) {
            return;
        }
        String tableName = functionTask.getTableName();
        if (StringUtils.isEmpty(tableName)) {
            return;
        }


        try {
            //根据自己的业务逻辑，执行存储过程


            callStoredProceduce("123", BigDecimal.valueOf(1234), BigDecimal.valueOf(4321));
        } catch (Exception e) {
            //记录错误信息、跑存储过程的那条记录置为不可跑状态。。。
        }
    }


    /**
     * 调用具体的存储过程
     *
     * @param storedProceducedName
     * @param startTime            自己的业务逻辑可有可无
     * @param endTime              自己的业务逻辑可有可无
     * @return
     */
    private String callStoredProceduce(String storedProceducedName, BigDecimal startTime, BigDecimal endTime) {
        //注意：callable表示调用存储过程，使用的比较少平时
//        <select id="callFunctionTaskNew"  resultType="java.lang.String" statementType="CALLABLE">
//                {call ${storedProceducedName}(#{startTime,mode=IN},#{endTime,mode=IN})}
//        </select>
        return "xxx";
    }

}
