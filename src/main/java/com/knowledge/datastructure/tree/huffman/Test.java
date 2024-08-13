package com.knowledge.datastructure.tree.huffman;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/11/30 21:26
 * @version:1.0
 */
public class Test {
    public static void main(String[] args) {
        int[] arr = {13, 7, 8, 3, 29, 6, 1};
        //node是赫夫曼树的根节点，所有的节点都链接在根节点上面了
        Node node = creatHuffmanTree(arr);
        //遍历
        if (node != null) {
            preOrder(node);
        }
    }

    /**
     * 创建赫夫曼树
     *
     * @param arr
     * @return 赫夫曼树的根节点
     */
    public static Node creatHuffmanTree(int[] arr) {
        List<Node> nodes = new ArrayList<>();
        //将arr的每一个元素构成Node,并将Node放入list中
        for (int value : arr) {
            nodes.add(new Node(value));
        }
        //最好List只会剩下一个根节点
        while (nodes.size() > 1) {
            //排序
            Collections.sort(nodes);
            //取出权值最小的两颗二叉树
            Node leftNode = nodes.get(0);
            Node rightNode = nodes.get(1);
            //构建一颗新的二叉树
            Node parent = new Node(leftNode.getValue() + rightNode.getValue());
            parent.setLeft(leftNode);
            parent.setRight(rightNode);
            //因为最小的两个已经取出，所有需要删除
            nodes.remove(leftNode);
            nodes.remove(rightNode);
            //加入新的节点入List中
            nodes.add(parent);
            //重新排序
            Collections.sort(nodes);
            System.out.println(nodes);
        }
        return nodes.get(0);
    }

    /**
     * 前序遍历 赫夫曼树
     *
     * @param root 赫夫曼树的根节点
     */
    public static void preOrder(Node root) {
        System.out.println(root);
        if (root.getLeft() != null) {
            preOrder(root.getLeft());
        }
        if (root.getRight() != null) {
            preOrder(root.getRight());
        }

    }
}
