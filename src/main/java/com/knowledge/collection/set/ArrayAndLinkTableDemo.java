package com.knowledge.collection.set;

/**
 * @description:模拟hashmap底层 数组+链表
 * @author:xiaoyige
 * @createTime:2021/11/18 20:30
 * @version:1.0
 */
public class ArrayAndLinkTableDemo {
    public static void main(String[] args) {
        //创建一个数组  HashMap默认大小16
        Node[] table=new Node[16];
        //创建节点
        Node john=new Node("john",null);
        table[2]=john;
        //jack节点
        Node jack=new Node("jack",null);
        //形成链表 将jack挂载到john后面
        john.next=jack;
        Node rose=new Node("rose",null);
        jack.next=rose;

    }
}

//节点：存放数组 ，可以指向下一个节点  从而形成链表
class Node {
    Object item;//存放数据
    Node next;//指向下一个节点

    public Node(Object item, Node next) {
        this.item = item;
        this.next = next;
    }
}
