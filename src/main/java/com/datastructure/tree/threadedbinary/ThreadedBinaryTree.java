package com.datastructure.tree.threadedbinary;

/**
 * 线索化 二叉树
 *
 * @author:xiaoyige
 * @createTime:2021/11/26 21:13
 * @version:1.0
 */
public class ThreadedBinaryTree {
    private HeroNode root;
    //为了实现线索化，需要创建指向当前节点的前驱节点
    private HeroNode pre;
    private HeroNode next;

    /**
     * 构造器 给根节点赋值
     *
     * @param root
     */
    public ThreadedBinaryTree(HeroNode root) {
        this.root = root;
    }

    /**
     * 中序线索化
     */
    public void threadedNodes(HeroNode heroNode) {
        //不能线索化
        if (heroNode == null) {
            return;
        }
        //线索化左子树
        threadedNodes(heroNode.getLeft());
        //线索化当前节点
        //处理当前节点的前驱节点
        if (heroNode.getLeft() == null) {
            //当前节点的左指针指向前驱节点
            heroNode.setLeft(pre);
            //修改当前节点的左指针类型，指向前驱节点
            heroNode.setLeftType(1);
        }
        //处理后继节点
        if (pre != null && pre.getRight() == null) {
            //前驱节点的右指针指向当前节点
            pre.setRight(heroNode);
            pre.setRightType(1);
        }
        //每处理一个节点后，让当前节点是下一个节点的前驱节点
        pre = heroNode;

        //线索化右子树
        threadedNodes(heroNode.getRight());
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

    //遍历线索化二叉树  按照什么顺序线索化的 那么就按照什么顺序遍历
    public void threadedList() {
        //从root开始遍历
        HeroNode node = root;
        while (node != null) {
            //找到leftType==1的节点
            //后面随着遍历而变化，当leftType==1,说明该节点是按照线索化处理后的有效节点
            while (node.getLeftType() == 0) {
                node = node.getLeft();
            }
            System.out.println(node);
            //如果当前节点的右指针指向的是后继节点，就一直输出
            while (node.getRightType() == 1) {
                //获取当前节点的后继节点
                node = node.getRight();
                System.out.println(node);
            }
            //替换遍历的节点
            node = node.getRight();
        }
    }
}
