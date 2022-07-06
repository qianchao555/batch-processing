package com.util.juc;


import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @ClassName MultiThreadUtil
 * @Author qianchao
 * @Date 2022/7/6
 * @Version V1.0
 * @description 多线程计算，主线程等待计算结果，并进行统计
 * 思路：
 * 1. 需要多少个线程处理=>暂定List多少元素就多少个线程来处理
 * 2. 处理的结果如何存放=>所以计算结果放到一个队列里面，最后统计
 * 3. 定义一个countdownlatch用于等待所有线程就绪后，控制所有线程并发执行
 * 4. 顶一个countdownlatch用于控制，主线程等待所有线程计算完毕后统计数据
 * D传入被处理的数据，R返回的数据类型
 **/
public abstract class MultiThreadUtil<D, R> {
    private ExecutorService executorService;
    private final BlockingQueue<Future<R>> futureBlockingQueue = new LinkedBlockingQueue<>();
    private final CountDownLatch startCountDownLatch = new CountDownLatch(1);
    private final CountDownLatch endCountDownLatch;
    //被处理的数据
    private List<D> listData;

    /**
     * 初始化
     *
     * @param list
     */
    public MultiThreadUtil(List<D> list) {
        if (!CollectionUtils.isEmpty(list)) {
            this.listData = list;
            this.executorService = Executors.newFixedThreadPool(list.size());
            this.endCountDownLatch = new CountDownLatch(list.size());
        } else {
            listData = null;
            executorService = null;
            endCountDownLatch = null;
        }
    }

    /**
     * 定义任务，每个任务都会被阻塞，直到被通知执行才开始执行
     */
    private abstract class DataProcessTask implements Callable<R> {
        //当前线程号
        private int currentThread;
        //当前线程处理的数据
        private D data;

        public DataProcessTask(int currentThread, D data) {
            this.currentThread = currentThread;
            this.data = data;
        }

        /**
         * @return r
         * @throws Exception
         */
        @Override
        public R call() throws Exception {
            //任务阻塞，只要等待countdownLatch计数器为0后才开始执行
            startCountDownLatch.await();
            R r = executeTask(currentThread, data);
//            R r = businessCodeExecute(currentThread, data);
            //当前线程处理完成，结束控制计数器减1
            endCountDownLatch.countDown();
            return r;
        }

        /**
         * 每一个线程执行的功能，这一层可要可不要
         *
         * @param currentThread
         * @param data
         * @return R
         */
        public abstract R executeTask(int currentThread, D data);
    }

    /**
     * 业务代码开始执行，即每一个线程执行的功能，需要调用者实现
     *
     * @param currentThread
     * @param data
     * @return
     */
    public abstract R businessCodeExecute(int currentThread, D data);

    /**
     * 启动每一个线程，并得到返回结果
     *
     * @return
     */
    public List<R> getProcessResult() throws InterruptedException, ExecutionException {
        List<R> resultList = new ArrayList<>();
        if (listData != null && listData.size() > 0) {
            try {
                int nThread = listData.size();
                for (int i = 0; i < nThread; i++) {
                    //提交任务给线程池
                    Future<R> future = executorService.submit(new DataProcessTask(i, listData.get(i)) {
                        @Override
                        public R executeTask(int currentThread, D data) {
                            return businessCodeExecute(currentThread,data);
                        }
                    });
//                    //提交任务给线程池
//                    Future<R> future = executorService.submit(
//                            new DataProcessTask(i, listData.get(i)) {
//                            });
                    //每一个线程的返回结果
                    futureBlockingQueue.offer(future);
                }
                //所有任务都准备完毕,启动门计数器减1,此时计数器为0,所有线程开始执行任务
                startCountDownLatch.countDown();
                //主线程阻塞等待所有子线程执行完毕
                endCountDownLatch.await();
                //统计计算结果
                for (Future<R> future : futureBlockingQueue) {
                    R r = future.get();
                    resultList.add(r);
                }
            } finally {
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
        return resultList;
    }

}
