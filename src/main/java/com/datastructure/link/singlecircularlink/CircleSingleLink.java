package com.datastructure.link.singlecircularlink;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/11/24 21:37
 * @version:1.0
 */
public class CircleSingleLink {
    //first节点，当前没有编号
    private Boy first;

    /**
     * 添加Boy节点，构建成环形链表
     *
     * @param nums 添加多少个节点
     */
    public void addBoy(int nums) {
        if (nums < 1) {
            System.out.println("添加的节点个数至少一个！！");
            return;
        }
        Boy curBoy = null;
        for (int i = 1; i <= nums; i++) {
            Boy boy = new Boy(i);
            //如果是第一个
            if (i == 1) {
                first = boy;
                //第一个Boy,指向自己形成环
                first.setNext(first);
                curBoy = first;
            } else {
                //形成环了
                curBoy.setNext(boy);
                boy.setNext(first);
                //当前节点右移
                curBoy = boy;
            }

        }
    }

    public void showCircleLink() {
        if (first == null) {
            System.out.println("单向循环链表为空");
            return;
        }
        //first不能改变，所以需要辅助指针进行遍历
        Boy curBoy = first;
        while (true) {
            System.out.println("boy的编号:" + curBoy.getNo());
            //到了最后一个节点了
            if (curBoy.getNext() == first) {
                break;
            }
            curBoy = curBoy.getNext();
        }
    }

    /**
     * @param startNo  从第几个开始
     * @param countNum 数几下
     * @param nums     节点个数
     */
    public void countBoy(int startNo, int countNum, int nums) {
        if (first == null || startNo < 1 || startNo > nums || countNum < 1) {
            System.out.println("参数有误，重新输入");
        }
        //尾指针，最初在第一个节点
        Boy helper = first;
        while (true) {
            //说明指向了最后一个节点
            if (helper.getNext() == first) {
                break;
            }
            //后移
            helper = helper.getNext();
        }
        //报数前，将first和helper移动startNo-1次 (第几个人开始报数)
        for (int j = 0; j < startNo - 1; j++) {
            first = first.getNext();
            helper = helper.getNext();
        }
        //报数时，first和helper 指针同时移动 countNum-1次，然后出圈
        while (true) {
            //圈里只有一个人了
            if (helper == first) {
                break;
            }
            //first和helper 指针同时移动
            for (int j = 0; j < countNum - 1; j++) {
                first = first.getNext();
                helper = helper.getNext();
            }
            //此时first指向的节点为应该出圈的小孩
            System.out.println("小孩%d出圈" + first.getNo());
            //出圈
            first = first.getNext();
            helper.setNext(first);
        }
        System.out.println("最后存在的小孩编号为%d" + first.getNo());
    }
}
