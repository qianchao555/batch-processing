package datastructure.link.singlecircularlink;

/**
 * @ClassName MyJosephuCicle
 * @Author qianchao
 * @Date 2021/11/25
 * @Version designpattern V1.0
 **/
public class MyJosephuCicle {
    private Boy first;

    public void addNode(int nums) {
        if (nums < 1) {
            System.out.println("节点个数不能少于一个！");
            return;
        }
        //当前添加的最新节点
        Boy curNode = null;
        for (int i = 1; i <= nums; i++) {
            Boy boy = new Boy(i);
            if (i == 1) {
                first = boy;
                first.setNext(first);
                curNode = boy;
            } else {
                //形成环结
                curNode.setNext(boy);
                boy.setNext(first);
                //最新节点为当前节点
                curNode = boy;
            }
        }

    }

    public void countNode(int k, int m, int nums) {
        //尾指针
        Boy tail = first;
        //找到尾节点
        while (true) {
            if (tail.getNext() == first) {
                break;
            }
            tail = tail.getNext();
        }
        //开始报数之前，需要移动first,tail指针  ：有可能不是第一个开始报数
        for (int i = 0; i < k - 1; i++) {
            first = first.getNext();
            tail = tail.getNext();
        }
        //开始报数
        while (true) {
            //只有一个人就退出
            if (tail == first) {
                break;
            }
            //报数是同时移动first,tail指针
            for (int i = 0; i < m - 1; i++) {
                first = first.getNext();
                tail = tail.getNext();
            }
            //出圈
            System.out.println("出圈："+first.getNo());
            first=first.getNext();
            tail.setNext(first);
        }
        System.out.println("最后出圈的人："+first.getNo());
    }


}
