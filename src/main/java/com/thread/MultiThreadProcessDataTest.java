package com.thread;

import com.alibaba.fastjson.JSON;
import com.util.juc.MultiThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName MultiThreadProcessDataTest
 * @Author qianchao
 * @Date 2022/7/6
 * @Version V1.0
 **/
@Slf4j
public class MultiThreadProcessDataTest {
    @Test
    public  void multiThreadTest( ) {
        List<Integer> moneyInteger = new ArrayList<>();

        for (int i = 0; i <= 50; i++) {
//            moneyInteger.add((int) (Math.random() * i));
            moneyInteger.add(i);
        }
        //list太大需要分割成块，来对每一个块分别计算
        List<List<Integer>> blockLists = splitList(moneyInteger, 10);

        //优化？创建一个线程池的时候
        ExecutorService executorService = Executors.newFixedThreadPool(blockLists.get(0).size());

        //统计最终结果
        List<Integer> sumList = new ArrayList<>();
        //多线程计算

        //优化？创建一个线程池的时候
        try {


        for (int i = 0; i < blockLists.size(); i++) {
            //处理当前块List
            List<Integer> blockList = blockLists.get(i);
//            MultiThreadUtil<Integer, Integer> multiThreadUtil = new MultiThreadUtil<Integer, Integer>(blockList) {

            MultiThreadUtil<Integer, Integer> multiThreadUtil = new MultiThreadUtil<Integer, Integer>(blockList,executorService) {
                @Override
                public Integer businessCodeExecuteTask(int currentThread, Integer data) {
                    try {
                        //模拟处理时间
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return data;
                }
            };
            try {
                List<Integer> processResult = multiThreadUtil.getProcessResult();

                Integer sum = processResult.stream().mapToInt(x -> x).sum();
//                for (int j = 0; j < processResult.size(); j++) {
//                    sum += processResult.get(j);
//                }


                log.info("list块大小为{},内容为：{}多线程计算结果：{}", blockList.size(),JSON.toJSONString(blockList), sum);
                sumList.add(sum);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        //2种方式求和
        Integer sumCount = sumList.stream().mapToInt(x -> x).sum();
//        for (int i = 0; i < sumList.size(); i++) {
//            sumCount+=sumList.get(i);
//        }
        log.info("最终统计结果为：" + sumCount);
        }

        finally {
            //关闭线程池，此时并未关闭，不能再提交任务到线程池，线程池里面的任务继续执行
            executorService.shutdown();
            //等待所有任务完成
            for (; ; ) {
                if (executorService.isTerminated()) {
                    break;
                }
            }
        }

    }

    /**
     * @param moneyInteger  集合数据
     * @param splitListSize 几个分割到一组
     * @return 分割后的集合，例如：23条数据，10个分割为1组；则：List<List<T>>  2个List<T>大小为10 ，一个List<T>大小为3
     */
    private static <T> List<List<T>> splitList(List<T> moneyInteger, int splitListSize) {



        if (CollectionUtils.isEmpty(moneyInteger)) {
            return Collections.emptyList();
        }
        //计算切分次数-即：将List切分为多少个List块
        int maxSize = (moneyInteger.size() + splitListSize - 1) / splitListSize;


        List<List<T>> collect =
                Stream.iterate(0, n -> n + 1)//创建一个无限流
                        .limit(maxSize)//截断流，使其不超过指定数量
                        .parallel()
                        //将0、1、2、3、4.....分别映射为一个有范围大小的List<T>
                        .map(x ->
                                moneyInteger.parallelStream()
                                        .skip(x * splitListSize)
                                        .limit(splitListSize)
                                        .collect(Collectors.toList())
                        )
                        .filter(y -> !y.isEmpty())
                        .collect(Collectors.toList());
        return collect;
    }

}
