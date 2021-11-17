package collection.list;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/11/17 21:14
 * @version:1.0
 */
public class LinkedListDemo {
    public static void main(String[] args) {
        //模拟双向链表
        Node jack = new Node("jack");
        Node tom = new Node("Tom");
        Node hsp = new Node("hsp");

        //连接三个节点，形成双向链表
        //Jack->tom->hsp
        jack.next = tom;
        tom.next = hsp;
        //hsp->tom->jack
        hsp.pre = tom;
        tom.pre = jack;
        //头结点
        Node first = jack;
        //尾节点
        Node last = hsp;

        //添加一个smith   tom 和 hsp 直接插入   tom smith hsp
        //1. 创建Node节点
        Node smith =new Node("smith");
        smith.next=hsp;
        smith.pre=tom;
        tom.next=smith;
        hsp.pre=smith;

        //从头到尾遍历
        while (true) {
            //尾节点的next为null
            if (first == null) {
                break;
            }
            System.out.println(first);
            first = first.next;
        }

        //从尾到头
        while (true) {
            //尾节点的next为null
            if (last == null) {
                break;
            }
            System.out.println(last);
            last = last.pre;
        }




    }
}

/**
 * 双向链表的一个节点
 */
class Node {
    public Object item;//真正存放数据的地方
    public Node next;//指向下一个节点
    public Node pre;//指向上一个节点

    public Node(Object item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "Node:"+item;
    }
}
