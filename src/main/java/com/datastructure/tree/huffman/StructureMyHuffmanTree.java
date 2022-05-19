package com.datastructure.tree.huffman;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @ClassName MyHuffmanTreeStrcute
 * @Author qianchao
 * @Date 2021/12/1
 * @Version  V1.0 V1.0
 **/
public class StructureMyHuffmanTree {
    public static Node createHuffman(int[] arr) {
        //将数组中的数据当作节点添加进List中
        ArrayList<Node> nodes = new ArrayList<>();
        for (int o : arr) {
            nodes.add(new Node(o));
        }
        Collections.sort(nodes);
        while (true) {
            //获取最小的两个节点
            Node left = nodes.get(0);
            Node right = nodes.get(1);
            //两个节点构成一个新的父节点
            Node parent = new Node(left.getValue() + right.getValue());
            parent.setLeft(left);
            parent.setRight(right);
            //List中删除最小的两个节点
            nodes.remove(left);
            nodes.remove(right);
            //组合成的新节点加入List中
            nodes.add(parent);
            //重新排序
            Collections.sort(nodes);
            System.out.println(nodes);
            if (nodes.size() <= 1) {
                break;
            }
        }

        return nodes.get(0);
    }

}
