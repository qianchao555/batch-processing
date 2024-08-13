package com.knowledge.datastructure.tree;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/11/26 21:13
 * @version:1.0
 */
public class BinaryTree {
    private HeroNode root;

    /**
     * 构造器 给根节点赋值
     *
     * @param root
     */
    public BinaryTree(HeroNode root) {
        this.root = root;
    }

    //前序
    public void preOrder() {
        if (root != null) {
            root.preOrder();
        } else {
            System.out.println("二叉树为空");
        }
    }

    //中序
    public void infixOrder() {
        if (root != null) {
            root.infixOrder();
        } else {
            System.out.println("二叉树为空");
        }
    }

    //后序
    public void postOrder() {
        if (root != null) {
            root.postOrder();
        } else {
            System.out.println("二叉树为空");
        }
    }

    //前序 查找
    public HeroNode preOrderSearch(int no) {
        if (root != null) {
            return root.preOrderSearch(no);
        } else {
            return null;
        }
    }

    //中序
    public HeroNode infixOrderSearch(int no) {
        if (root != null) {
            return root.infixOrderSearch(no);
        } else {
            return null;
        }
    }

    //后序
    public HeroNode postOrderSearch(int no) {
        if (root != null) {
            return root.postOrderSearch(no);
        } else {
            return null;
        }
    }

    //删除节点
    public void del(int no) {
        if (root != null) {
            //根节点是要寻找的节点
            if (root.getNo() == no) {
                root = null;
            } else {
                //递归删除
                root.deleteNode(no);
            }
        } else {
            System.out.println("空树！");
        }
    }
}
