package com.knowledge.datastructure.link.singlelinktable;

/**
 * 单链表
 *
 * @author:xiaoyige
 * @createTime:2021/11/22 20:55
 * @version:1.0
 */
public class SingleLinkedListDemo {
    public static void main(String[] args) {
        HeroNode heroNode1 = new HeroNode(1, "宋江", "及时雨");
        HeroNode heroNode2 = new HeroNode(2, "卢俊义", "玉麒麟");
        HeroNode heroNode3 = new HeroNode(3, "吴用", "智多星");
        HeroNode heroNode4 = new HeroNode(4, "林冲", "豹子头");
        //加入到链表中
        SingleLinkedList singleLinkedList = new SingleLinkedList();
//        singleLinkedList.addHeroNode(heroNode1);
//        singleLinkedList.addHeroNode(heroNode2);
//        singleLinkedList.addHeroNode(heroNode3);
//        singleLinkedList.addHeroNode(heroNode4);
        //带有编号的插入
        singleLinkedList.addHeroNodeByNo(heroNode1);
        singleLinkedList.addHeroNodeByNo(heroNode4);
        singleLinkedList.addHeroNodeByNo(heroNode2);
        singleLinkedList.addHeroNodeByNo(heroNode3);
        //不能插入
//        singleLinkedList.addHeroNodeByNo(heroNode3);

        //测试修改
        HeroNode newHeroNode=new HeroNode(2,"小卢","玉麒麟~~~");
        singleLinkedList.update(newHeroNode);
        //查看
        singleLinkedList.showLinkedList();

    }
}

//定义链表，管理Node
class SingleLinkedList {

    HeroNode head = new HeroNode(0, "", "");

    /**
     * 添加节点
     * 1. 不考虑108好汉的排名时
     * 将最后一个节点的next指向添加的节点
     */
    public void addHeroNode(HeroNode heroNede) {
        //head节点不能动，所以需要辅助遍历
        HeroNode temp = head;
        //遍历链表，找到最后
        while (true) {
            //链表尾部
            if (temp.next == null) {
                break;
            }
            //没有找到最后，将temp后移
            temp = temp.next;
        }
        //退出循环时，temp指向了链表最后。将最后这个节点的next,指向新的节点
        temp.next = heroNede;
    }

    /**
     * 添加节点
     * 1. 考虑108好汉的排名,按照编号顺序添加
     * 思路：
     * 1. 需要找到新节点插入的位置
     */
    public void addHeroNodeByNo(HeroNode heroNede) {
        //head节点不能动
        //temp：需要添加位置的前一个节点，否则插入不了
        HeroNode temp = head;
        //标识添加的节点编号是否存在
        boolean heroNoFlag = false;
        while (true) {
            if (temp.next == null) {
                break;
            }
            if (temp.next.no > heroNede.no) {
                //位置找到
//                temp.next = heroNede;
                break;
            } else if (temp.next.no == heroNede.no) {
                //需要添加的编号已经存在，不能添加
                heroNoFlag = true;
                break;
            }
            temp = temp.next;
        }
        if (heroNoFlag) {
            //不能添加
            System.out.println("已经存在这个英雄：" + heroNede);
        } else {
            //插入到链表中 =》temp的后面
            heroNede.next = temp.next;//新节点的下一个节点
            temp.next = heroNede;//上一个节点指向新节点
        }
    }

    /**
     * 修改节点信息 根据No修改
     */
    public void update(HeroNode heroNode) {
        //操作都要从头结点开始
        if (head.next == null) {
            System.out.println("节点为空！！");
            return;
        }
        HeroNode temp = head;
        //是否找到
        boolean findFlag = false;
        while (true) {
            //链表已经遍历链表完了
            if (temp == null) {
                break;
            }
            //找到要修改的节点
            if (temp.no == heroNode.no) {
                findFlag = true;
                break;
            }
            //继续往下找
            temp = temp.next;
        }
        if (findFlag) {
            temp.name = heroNode.name;
            temp.nickName = heroNode.nickName;
        } else {
            System.out.println("没有找到编号为：" + heroNode.no + " 的HeroNode");
        }
    }

    /**
     * 遍历链表
     */
    public void showLinkedList() {
        if (head.next == null) {
            System.out.println("链表为空！");
            return;
        }
        //头结点不能动
        HeroNode temp = head.next;
        while (true) {
            if (temp == null) {
                break;
            }
            //输出节点信息
            System.out.println(temp);
            //节点后移
            temp = temp.next;
        }
    }

}

class HeroNode {
    public int no;
    public String name;
    //昵称
    public String nickName;

    public HeroNode next;

    public HeroNode(int no, String name, String nickName) {
        this.no = no;
        this.name = name;
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "HeroNode{" +
                "no=" + no +
                ", name='" + name + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
