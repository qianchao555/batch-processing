package com.juc;

import com.util.juc.MultiThreadUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName MultiThreadProcessDataTest
 * @Author qianchao
 * @Date 2022/7/6
 * @Version   V1.0
 **/
public class MultiThreadProcessDataTest {
    public static void main(String[] args) {
        List<Integer> moneyInteger=new ArrayList<>();

        for(int i=0;i<10;i++){
            moneyInteger.add(i);
        }
        MultiThreadUtil<Integer,Integer> multiThreadUtil=new MultiThreadUtil<Integer, Integer>(moneyInteger) {
            @Override
            public Integer businessCodeExecuteTask(int currentThread, Integer data) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return data+10;
            }
        };
        try {
            List<Integer> processResult = multiThreadUtil.getProcessResult();
            Integer sum=0;
            for (int i = 0; i < processResult.size(); i++) {
                sum+=processResult.get(i);
            }
            System.out.println("多线程计算结果："+sum);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }



    }

}
