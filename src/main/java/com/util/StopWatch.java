package com.util;

/**
 * @ClassName StopWatch
 * @Author qianchao
 * @Date 2022/6/8
 * @Version OPRA V1.0
 **/
public class StopWatch {
    private long startTimeNanos;
    private String taskName;

    /**
     * 任务开始时间
     * @param taskName
     */
    public void start(String taskName){
        this.taskName=taskName;
        this.startTimeNanos=System.nanoTime();
    }

    /**
     * 任务结束
     */
    public void stop(){
        long endTime=System.nanoTime()-this.startTimeNanos;
    }

}
