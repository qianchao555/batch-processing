package com.datastructure.link.singlecircularlink;

/**
 * 单向环形链表
 * @author:xiaoyige
 * @createTime:2021/11/24 21:17
 * @version:1.0
 */
public class SingleCircDemo {
    public static void main(String[] args) {
        CircleSingleLink circleSingleLink=new CircleSingleLink();
        //加入5个节点
//        circleSingleLink.addBoy(5);
//        circleSingleLink.showCircleLink();
//        circleSingleLink.countBoy(1,2,5);
        MyJosephuCicle myJosephuCicle=new MyJosephuCicle();
        myJosephuCicle.addNode(5);
        myJosephuCicle.countNode(1,2,5);
    }
}
