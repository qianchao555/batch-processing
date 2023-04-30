package com.util.juc;


import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public abstract class MultiThreadUtil<D, R> {


    private ExecutorService executorService;


    //阻塞队列，入队、出队实现原理：都用到了Lock锁的多条件Condition阻塞控制
    private final BlockingQueue<Future<R>> futureBlockingQueue = new LinkedBlockingQueue<>();

    //启动门，用于控制任务准备完毕后，统一放开去执行
    private final CountDownLatch startCountDownLatch = new CountDownLatch(1);

    //结束门，等待所有任务执行完成
    private final CountDownLatch endCountDownLatch;


    //被处理的数据
    private List<D> listData;

    /**
     * 初始化
     *
     * @param list
     */
//    public MultiThreadUtil(List<D> list ) {

      //优化？ 创建一个线程池的时候
    public MultiThreadUtil(List<D> list,ExecutorService executorService) {
        if (!CollectionUtils.isEmpty(list)) {
            this.listData = list;
            //这里有问题，具体场景具体分析，不能利用List的大小来创建线程个数
//            this.executorService = Executors.newFixedThreadPool(list.size());

            //创建一个线程池的时候
            this.executorService = executorService;

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
            //线程启动后，当前线程阻塞，当前任务阻塞在这里，等待startCountDownLatch计数器为0后才开始执行

            //初次阻塞在这里的任务数量受制于cpu的核心数，待startCountDownLatch计数器0后，后续任务不再阻塞
            log.info("我是线程："+currentThread+"的call(),开始调用我了。。。。");
            startCountDownLatch.await();

            //执行任务
            // R r = businessCodeExecute(currentThread, data);
            R r = null;
            try {
                r = executeTask(currentThread, data);
            } finally {
                //当前线程处理完成，结束控制计数器减1
                log.info("当前线程号"+currentThread+"处理结束，处理结果为：{},并调用了countDown()",r);
                endCountDownLatch.countDown();
            }
            return r;
        }

        /**
         * 每一个线程执行的功能
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
     * @param currentThread 线程号
     * @param data          每个线程需要处理的数据
     * @return R 传入数据data的处理结果
     */
    public abstract R businessCodeExecuteTask(int currentThread, D data);

    /**
     * 启动每一个线程，并得到返回结果
     *
     * @return
     */
    public List<R> getProcessResult() throws InterruptedException, ExecutionException {
        List<R> resultList = new ArrayList<>();
        if (listData != null && listData.size() > 0) {
//            try {
                int nThread = listData.size();

                //将任务提交到线程池，执行
                for (int i = 0; i < nThread; i++) {
                    //提交任务给线程池
                    Future<R> future = executorService.submit(new DataProcessTask(i, listData.get(i)) {
                        @Override
                        public R executeTask(int currentThread, D data) {
                            return businessCodeExecuteTask(currentThread, data);
                        }
                    });

                    //将每个线程得到的Future实例添加至队列
                    futureBlockingQueue.add(future);
                }

                TimeUnit.SECONDS.sleep(2);

                //所有任务都准备完毕,启动门计数器减1,此时计数器为0,唤醒call()方法，所有线程开始执行任务
                startCountDownLatch.countDown();


//                TimeUnit.SECONDS.sleep(5);

                //主线程阻塞等待所有子线程执行完毕
                log.info("等待当前List块大小为{}的所有线程处理任务中。。。。。",listData.size());
                endCountDownLatch.await();
                log.info("当前List块大小为{}的所有线程处理完成！",listData.size());


                //最后统计计算结果
                for (Future<R> future : futureBlockingQueue) {
                    R r = future.get();
                    resultList.add(r);
                }
//            }
//            finally {
//                //关闭线程池，此时并未关闭，不能再提交任务到线程池，线程池里面的任务继续执行
//                executorService.shutdown();
//                //等待所有任务完成
//                for (; ; ) {
//                    if (executorService.isTerminated()) {
//                        break;
//                    }
//                }
//            }
        }
        return resultList;
    }

}
