package datastructure.link.singlecircularlink;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/11/24 21:35
 * @version:1.0
 */
public class Boy {
    private int no;//编号
    private Boy next;

    public Boy(int no) {
        this.no = no;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public Boy getNext() {
        return next;
    }

    public void setNext(Boy next) {
        this.next = next;
    }

}
