package com.util;

import lombok.Data;

import java.util.Set;
import java.util.TreeSet;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/5/10 23:19
 * @version:1.0
 */
@Data
public class SnowFlake {

    private long workerId;//机房的机器
    private long datacenterId;//机房
    private long sequence;

    //构造函数seq参数可要可不要
    public SnowFlake(long workerId, long datacenterId) {
        // 检查传递进来的机房id和机器id不能超过32，不能小于0
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.sequence = sequence;
    }

    //初始时间戳(纪元） 一般为当前时间2022-05-10 22:59:27
//    private long initEpoch = 1652194767190L;
//    2062-05-10 22:58:49
    private long initEpoch = 2914498729000L;
    // 记录最后使用的毫秒时间戳，主要用于判断是否同一毫秒，以及用于服务器时钟回拨判断
    private long lastTimestamp = -1L;
    //datacenterId占用的位数
    private long datacenterIdBits = 5L;
    //workerId占用的位数
    private long workerIdBits = 5L;

    // dataCenterId占用5个比特位，最大值31
    // 0000000000000000000000000000000000000000000000000000000000011111
    // 这个是二进制运算，就是5 bit最多只能有31个数字，也就是说机器id最多只能是32以内
//    private long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private long maxWorkerId = ~(-1L << workerIdBits);

    // workId占用5个比特位，最大值31
    // 0000000000000000000000000000000000000000000000000000000000011111
    // 这个是一个意思，就是5 bit最多只能有31个数字，机房id最多只能是32以内
//    private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    private long maxDatacenterId = ~(-1L << datacenterIdBits);

    //序列号，最后12位
    private long sequenceBits = 12L;
    // 同一毫秒内的最新序号，最大值可为 2^12 - 1 = 4095
//    private long sequenceMask = -1L ^ (-1L << sequenceBits);
    private long sequenceMask = ~(-1L << sequenceBits);

    //workerId需要左移的位数
    private long workerIdShift = sequenceBits;
    //datacenterId需要左移的位数
    private long datacenterIdShift = sequenceBits + workerIdBits;
    //时间戳需要左移的位数：12+5+5
    private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;


    /**
     * 通过雪花算法生成下一个id,需要进行同步
     */
    public synchronized long nextId() {
        // 获取当前时间戳，单位是毫秒
        long timestamp = System.currentTimeMillis();

        // 当前时间小于上一次生成id使用的时间，可能出现服务器时钟回拨问题
        if (timestamp < lastTimestamp) {
            System.err.printf("可能出现服务器时钟回拨问题，请检查服务器时间。当前服务器时间戳：%d，上一次使用时间戳：%d", lastTimestamp);
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
                    lastTimestamp - timestamp));
        }

        // 0
        // 在同一个毫秒内，又发送了一个请求生成一个id，0 -> 1
        // 还是在同一毫秒内，则将序列号递增1，序列号最大值为4095
        if (lastTimestamp == timestamp) {
            // 这个意思是说一个毫秒内最多只能有4096个数字，无论你传递多少进来，这个位运算保证始终就是在4096这个范围内，
            //避免你自己传递个sequence超过了4096这个范围

            // 序列号的最大值是4095，使用掩码（最低12位为1，高位都为0）进行位与运行后如果值为0，则自增后的序列号超过了4095
            // 那么就使用新的时间戳
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // 不在同一毫秒内，则序列号重新从0开始，序列号最大值为4095
            sequence = 0;
        }

        // 记录一下最近一次生成id的时间戳，单位是毫秒
        lastTimestamp = timestamp;

        // 核心算法，将不同部分的数值移动到指定的位置，然后进行或运行

        // 这儿就是将时间戳左移，放到41 bit那儿；将机房id左移放到5 bit那儿；将机器id左移放到5 bit那儿；将序号放最后10 bit；最后拼接起来成一个64 bit的二进制数字，转换成10进制就是个long型
        return ((timestamp - initEpoch) << timestampLeftShift) |
                (datacenterId << datacenterIdShift) |
                (workerId << workerIdShift) |
                sequence;
    }

//  0 | 0001100 10100010 10111110 10001001 01011100 00 | 10001 | 1 1001 | 0000 00000000

    /**
     * 获取指定时间戳的接下来的时间戳，也可以说是下一毫秒
     *
     * @param lastTimestamp 指定毫秒时间戳
     * @return 时间戳
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    //---------------测试---------------
    public static void main(String[] args) {
        SnowFlake worker = new SnowFlake(30, 30);
        for (int i = 0; i < 5; i++) {
            System.out.println(Long.toBinaryString(worker.nextId()));
        }

        // 生成50个id
        Set<Long> set = new TreeSet<>();
        for (int i = 0; i < 50; i++) {
            set.add(worker.nextId());
        }
//        System.out.println(set.size());
//        System.out.println(set);

        // 验证生成100万个id需要多久
        long startTime = System.currentTimeMillis();
        for( int i = 0; i< 1000000;i++) {
            worker.nextId();
        }
        System.out.println(System.currentTimeMillis()-startTime);
    }



}

