package com.datastructure.tree.arrbinarytree;

/**
 * 以二叉树的前序遍历 遍历数组中的元素
 * @author:xiaoyige
 * @createTime:2021/11/27 14:52
 * @version:1.0
 */
public class ArrBinaryTree {
    //存储二叉树节点的数组
    private int[] arr;

    public ArrBinaryTree(int[] arr) {
        this.arr = arr;
    }

    /**
     * @param index 数组下标
     */
    public void preOrder(int index) {
        if (arr == null || arr.length == 0) {
            System.out.println("数组为空，不能按照二叉树的前序遍历");
        }
        //输出当前root这个元素  ==>对应数组中的arr[0]
        System.out.println(arr[index]);
        //向左递归遍历
        if (2 * index + 1 < arr.length) {
            preOrder(2 * index + 1);
        }
        //向右递归遍历
        if (2 * index + 2 < arr.length) {
            preOrder(2 * index + 2);
        }
    }
}
