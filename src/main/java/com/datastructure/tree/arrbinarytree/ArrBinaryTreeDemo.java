package com.datastructure.tree.arrbinarytree;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/11/27 14:52
 * @version:1.0
 */
public class ArrBinaryTreeDemo {
    public static void main(String[] args) {
        int []arr={1,2,3,4,5,6,7};
        ArrBinaryTree arrBinaryTree=new ArrBinaryTree(arr);
        //前序遍历 数组中的元素
        arrBinaryTree.preOrder(0);
    }
}
