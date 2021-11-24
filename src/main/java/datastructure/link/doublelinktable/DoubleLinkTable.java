package datastructure.link.doublelinktable;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/11/24 20:26
 * @version:1.0
 */
public class DoubleLinkTable {
    private HeroNode2 head = new HeroNode2(0, "", "");

    //返回头结点
    public HeroNode2 getHead() {
        return head;
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
        HeroNode2 temp = head.next;
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

    /**
     * 添加节点 默认添加到最后面
     */
    public void addHeroNode(HeroNode2 heroNode) {
        //head节点
        HeroNode2 temp = head;
        //遍历找到最后一个节点
        while (true) {
            if (temp.next == null) {
                break;
            }
            temp = temp.next;
        }
        //插入到链表中,形成双向链表
        temp.next = heroNode;
        heroNode.pre = temp;
    }

    /**
     * 修改节点信息 根据No修改
     */
    public void update(HeroNode2 heroNode) {
        //操作都要从头结点开始
        if (head.next == null) {
            System.out.println("节点为空！！");
            return;
        }
        HeroNode2 temp = head.next;
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
     * 删除双向链表节点
     */
    public void delete(int no) {
        if (head.next == null) {
            return;
        }
        HeroNode2 temp = head.next;
        boolean flag = false;
        while (true) {
            if (temp == null) {
                return;
            }
            if (temp.no == no) {
                flag = true;
                break;
            }
            temp = temp.next;
        }
        if (flag) {
            temp.pre.next = temp.next;
            //有问题这里  最后一个节点会出现空指针
            //temp.next.pre=temp.pre;
            //不是最后一个节点才做这个操作
            if (temp.next != null) {
                temp.next.pre = temp.pre;
            }
        }

    }
}
