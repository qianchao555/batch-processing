package datastructure.tree.huffman;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/11/30 21:22
 * @version:1.0
 */
public class Node implements Comparable<Node> {
    //节点权值
    private int value;
    private Node left;
    private Node right;

    public Node(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    @Override
    public int compareTo(Node o) {
        //从小到大排序
        return this.value - o.value;

    }

    @Override
    public String toString() {
        return "Node{" +
                "value=" + value +
                '}';
    }
}
