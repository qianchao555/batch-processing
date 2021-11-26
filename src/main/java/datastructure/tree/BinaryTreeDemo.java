package datastructure.tree;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/11/26 21:04
 * @version:1.0
 */
public class BinaryTreeDemo {
    public static void main(String[] args) {

        //二叉树的节点
        HeroNode nodeRoot = new HeroNode(1, "宋江");
        HeroNode node2 = new HeroNode(2, "无用");
        HeroNode node3 = new HeroNode(3, "卢俊义");
        HeroNode node4 = new HeroNode(4, "林冲");
        nodeRoot.setLeft(node2);
        nodeRoot.setRight(node3);
        node3.setRight(node4);

        //手动创建二叉树，后边使用递归的方式自动创建
        BinaryTree root = new BinaryTree(nodeRoot);
//        System.out.println("前序：");
//        root.preOrder();
//
//        System.out.println("中序：");
//        root.infixOrder();
//
//        System.out.println("后序：");
//        root.postOrder();

        System.out.print("前序查找No=2：");
        HeroNode searchNode1 = root.preOrderSearch(3);
        System.out.println(searchNode1);
        System.out.print("中序查找No=2：");
        HeroNode searchNode2 = root.infixOrderSearch(3);
        System.out.println(searchNode2);
        System.out.print("后序查找No=2：");
        HeroNode searchNode3 = root.postOrderSearch(3);
        System.out.println(searchNode3);
    }

}
