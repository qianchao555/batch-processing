package com.knowledge.designpattern.behaviortype.state;

/**
 * 状态模式 测试类
 * @author:xiaoyige
 * @createTime:2021/12/6 20:37
 * @version:1.0
 */
public class Test {
    public static void main(String[] args) {
        //一个奖品
        Context context=new Context(1);
        //连续抽三次
        for (int i = 0; i < 2; i++) {
            System.out.println("第"+i+"次抽奖");
            //扣钱
            context.debuctMoney();
            //抽奖
            context.raffle();
        }
    }
}
