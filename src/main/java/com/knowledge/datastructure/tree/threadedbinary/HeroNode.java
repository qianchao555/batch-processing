package com.knowledge.datastructure.tree.threadedbinary;

public class HeroNode {
    private int no;
    private String name;

    //左子树
    private HeroNode left;

    //leftType==0 指向左子树，==1  指向前驱节点
    private int leftType;

    //rightType==0 指向右边子树，==1  指向后继节点
    private int rightType;
    //右子树
    private HeroNode right;

    public int getLeftType() {
        return leftType;
    }

    public void setLeftType(int leftType) {
        this.leftType = leftType;
    }

    public int getRightType() {
        return rightType;
    }

    public void setRightType(int rightType) {
        this.rightType = rightType;
    }

    public HeroNode(int no, String name) {
        this.no = no;
        this.name = name;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HeroNode getLeft() {
        return left;
    }

    public void setLeft(HeroNode left) {
        this.left = left;
    }

    public HeroNode getRight() {
        return right;
    }

    public void setRight(HeroNode right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "HeroNodeTree{" +
                "no=" + no +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * 前序遍历二叉树
     */
    public void preOrder() {
        //先打印父节点
        System.out.println(this);
        //递归遍历左子树
        if (this.left != null) {
            this.left.preOrder();
        }
        //递归向右子树遍历
        if (this.right != null) {
            this.right.preOrder();
        }
    }

    /**
     * 中序遍历
     */
    public void infixOrder() {
        //递归遍历左子树
        if (this.left != null) {
            this.left.infixOrder();
        }
        //根节点
        System.out.println(this);
        //递归遍历右子树
        if (this.right != null) {
            this.right.infixOrder();
        }
    }

    /**
     * 后序遍历
     */
    public void postOrder() {
        //递归遍历左子树
        if (this.left != null) {
            this.left.postOrder();
        }
        //递归遍历右子树
        if (this.right != null) {
            this.right.postOrder();
        }
        //根节点
        System.out.println(this);
    }

    /**
     * 前序遍历 查找 No的节点
     *
     * @param no
     * @return
     */
    public HeroNode preOrderSearch(int no) {
        System.out.println("进入前序遍历");
        //根节点
        if (this.getNo() == no) {
            return this;
        }
        HeroNode node = null;
        //左子节点递归查找
        if (this.left != null) {
            node = this.left.preOrderSearch(no);
        }
        if (node != null) {
            return node;
        }
        //右子节点递归查找
        if (this.right != null) {
            node = this.right.preOrderSearch(no);
        }
        //此时node可能为空，因为右边找完了也没有找到
        return node;
    }

    /**
     * 中序遍历 查找 No的节点
     *
     * @param no
     * @return
     */
    public HeroNode infixOrderSearch(int no) {
        HeroNode node = null;
        //左子节点递归查找
        if (this.left != null) {
            node = this.left.infixOrderSearch(no);
        }
        if (node != null) {
            return node;
        }
        System.out.println("进入中序遍历");
        //根节点
        if (this.getNo() == no) {
            return this;
        }
        //右子节点递归查找
        if (this.right != null) {
            node = this.right.infixOrderSearch(no);
        }
        //此时node可能为空，因为右边找完了也没有找到
        return node;
    }

    /**
     * 后序遍历 查找 No的节点
     *
     * @param no
     * @return
     */
    public HeroNode postOrderSearch(int no) {

        HeroNode node = null;
        //左子节点递归查找
        if (this.left != null) {
            node = this.left.postOrderSearch(no);
        }
        if (node != null) {
            return node;
        }

        //右子节点递归查找
        if (this.right != null) {
            node = this.right.postOrderSearch(no);
        }
        if (node != null) {
            return node;
        }
        System.out.print("进入后序遍历");
        //左右都没有找到，比较当前节点（当前root）
        if (this.getNo() == no) {
            return this;
        }
        //此时node可能为空，因为根节点找完了也没有找到
        return node;
    }

    /**
     * 递归删除节点
     * 叶子节点则直接删除
     * 非叶子节点则删除子树
     * <p>
     * 类似 前序遍历来删除
     */
    public void deleteNode(int no) {
        //左子节点不为空，并且左子节点是要删除的节点
        if (this.left != null && this.left.getNo() == no) {
            this.left = null;
            return;
        }
        //右子节点不为空，并且右子节点是要删除的节点
        if (this.right != null && this.right.getNo() == no) {
            this.right = null;
            return;
        }
        //左子树进行递归删除
        if (this.left != null) {
            this.left.deleteNode(no);
        }
        //右子树进行递归删除
        if (this.right != null) {
            this.right.deleteNode(no);
        }
    }
}